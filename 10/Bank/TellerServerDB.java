// David Riedell, dariedel@ncsu.edu

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class TellerServerDB extends UnicastRemoteObject implements TellerInterface {

	private Connection connection;
	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteStatement;
	private Statement selectAllStatement;

	public static void main(String[] args) throws Exception {

		try {
			new TellerServerDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	ConcurrentHashMap<Integer, CashAccount> accounts = new ConcurrentHashMap<Integer, CashAccount>();
	String newLine = System.lineSeparator();

	public TellerServerDB() throws Exception {
		super();	//call parent constructor first
		// Restore passwords collection from disk

//		try {
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("accounts.ser"));
////			accounts =  (ConcurrentHashMap<Integer, CashAccount>) ois.readObject();
//			ois.close();
//			//			System.out.println(accounts);
//		} catch (FileNotFoundException fnfe) {
//			System.out.println("accounts.ser is not found on disk, so we will be starting with an empty passwords collection.");
//
//		}

		//super();
		Naming.rebind("TellerServices", this);
		System.out.println("TellerServicesDB is up at: " + InetAddress.getLocalHost().getHostAddress());
		//		System.out.println(accounts);

		//		System.out.println("DB driver loading!");		
		Class.forName("com.ibm.db2j.jdbc.DB2jDriver");
		System.out.println("DB driver loaded!");
		connection = DriverManager.getConnection("jdbc:db2j:/Users/driedell/Downloads/database/database/QuoteDB");
		System.out.println("DB opened!"); 

		insertStatement = connection.prepareStatement("INSERT INTO BANK_ACCOUNTS "
				+ "(ACCOUNT_NUMBER, ACCOUNT_TYPE, CUSTOMER_NAME, BALANCE) " + "VALUES (?,?,?,?)");

		updateStatement = connection.prepareStatement("UPDATE BANK_ACCOUNTS " + "SET BALANCE = ? "
				+ "WHERE ACCOUNT_NUMBER = ?");

		deleteStatement = connection.prepareStatement("DELETE FROM BANK_ACCOUNTS "
				+ "WHERE ACCOUNT_NUMBER = ?");

		selectAllStatement = connection.createStatement();
		ResultSet rs = selectAllStatement.executeQuery("SELECT * FROM BANK_ACCOUNTS");

		while (rs.next()) { // read the next row of the ResultSet
			// get the column values for this row
			int    accountNumber = rs.getInt   ("ACCOUNT_NUMBER");
			String accountType   = rs.getString("ACCOUNT_TYPE");
			String customerName  = rs.getString("CUSTOMER_NAME");
			double balance       = rs.getDouble("BALANCE");

			System.out.println(" acctNum="+ accountNumber + " acctType=" + accountType
					+ " custName=" + customerName + " balance=" + balance);

			CashAccount ca;
			if (accountType.equalsIgnoreCase(Bank.CHECKING)) {
				ca = CheckingAccount.restoreFromDataBase(customerName, balance, accountNumber);
			} else if (accountType.equalsIgnoreCase(Bank.SAVINGS)) {
				ca = SavingsAccount.restoreFromDataBase(customerName, balance, accountNumber);
			} else {
				System.out.println("SYSTEM ERROR: account type " + accountType + " is not recognized when reading DB table BANK_ACCOUNTS in server constructor.");
				continue; // skip unrecognized account
			}

			accounts.put(accountNumber, ca);
			System.out.println(accounts);
		}

	}

	@Override
	public String openNewAccount(String accountType, String customerName) {
		CashAccount ca = null;
		if(accountType.equals(Bank.CHECKING)) {
			try {
				ca  = new CheckingAccount(customerName);
			} catch (Exception e) {
				return "ERROR: account type " + accountType + " is not recognized by the server." + " Call the IT Department!" + e.toString();
			}
		} else if(accountType.equals(Bank.SAVINGS)) {
			try {
				ca = new SavingsAccount(customerName);
			} catch (Exception e) {
				return "ERROR: account type " + accountType + " is not recognized by the server." + " Call the IT Department!" + e.toString();
			}
		}

		accounts.put(ca.getAccountNumber(), ca);


		// Add a new row to the DB table for this new account
		try {
			insertStatement.setInt   (1, ca.getAccountNumber());
			insertStatement.setString(2, accountType);
			insertStatement.setString(3, customerName);
			insertStatement.setDouble(4, 0); // initial balance for a new account
			insertStatement.executeUpdate();
		} catch (SQLException sqle) {
			return "ERROR: Unable to add new account to the data base."
					+ sqle.toString();
		}


		//		try {
		//			saveAccounts();
		//		} catch (IOException e) {
		//			return "ERROR: blah blah blah" ;
		//		}
		System.out.println("Account #" + ca.getAccountNumber() + " has been saved!");
		return ca.toString();

	}


	@Override
	public String processAccount(String processType, int accountNumber, double amount) {
		//		String statusString1 =  "Process type:  " + processType + ", Amount: $" + amount + ", Account number: " + accountNumber;
		CashAccount ca = accounts.get(accountNumber);
		if(ca.equals(null)) {
			return "Account not found!";
		}

		if(processType.equals(Bank.DEPOSIT)) {
			ca.deposit(amount);
			try {
				updateStatement.setDouble (1, ca.getBalance());
				updateStatement.setInt    (2, ca.getAccountNumber());
				updateStatement.executeUpdate();
			} catch (SQLException sqle) {
				return "ERROR: Server is unable to update account in the data base."
						+ sqle.toString();
			}



			//			try {
			//				saveAccounts();
			//			} catch (IOException e) {
			//				return "ERROR: bleh bleh bleh" ;		
			//			}
		}
		else if(processType.equals(Bank.WITHDRAW)) {
			try {
				ca.withdraw(amount);
			} catch (OverdraftException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				updateStatement.setDouble (1, ca.getBalance());
				updateStatement.setInt    (2, ca.getAccountNumber());
				updateStatement.executeUpdate();
			} catch (SQLException sqle) {
				return "ERROR: Server is unable to update account in the data base."
						+ sqle.toString();
			}

			//			try {
			//				saveAccounts();
			//			} catch (IOException e) {
			//				return "ERROR: bluh bluh bluh" ;		
			//			}
			if(amount > ca.getBalance()) {
				return "Insufficient Funds";
			}
			else{
				return "ERROR: Transaction type: " + processType + " is not recognized by the server.";
			}
		}
		return ca.toString();
	}

	@Override
	public String showAccount(int accountNumber) {
		CashAccount ca = accounts.get(accountNumber);
		if(ca == null) {
			return "Account Not Found!";
		} else {
			return accounts.get(accountNumber).toString();
		}
	}

	@Override
	public String showAccount(String customerName) {
		CashAccount[] accountList = accounts.values().toArray(new CashAccount[0]);

		TreeSet<String> hitList = new TreeSet();
		for(CashAccount i : accountList) {
			String temp = i.getCustomerName();
			temp = temp.toUpperCase();
			if(temp.startsWith(customerName.toUpperCase())) {
				hitList.add(i.toString());
			}
		}

		if(hitList.isEmpty()) {
			return "No accounts starting with the entered name were found.";
		}

		String hitString = "";
		for(String a : hitList) {
			hitString = hitString + a;
			hitString = hitString + newLine;
		}
		return  hitString;
	}

	@Override
	public String closeAccount(int accountNumber, String customerName) {
		CashAccount ca = accounts.get(accountNumber);

		if(ca == null) {
			return "Account Not Found!";
		}
		if(!(customerName.equalsIgnoreCase(ca.getCustomerName()))) {
			return "Entered name does not match account name.";
		}
		if(!(ca.getBalance() == 0.00)) {
			return "Account balance is not zero.";
		}

		accounts.remove(accountNumber);
		try {
			deleteStatement.setInt(1, accountNumber);
			deleteStatement.executeUpdate();
		} catch (SQLException sqle) {
			return "ERROR: Server is unable to delete account from the data base." + sqle.toString();
		}

		//		try {
		//			saveAccounts();
		//		} catch (IOException e) {
		//			return "ERROR message returned to teller client." ;
		//		}
		System.out.println(ca.getBalance());
		return "Account #" + ca.getAccountNumber() + " is being closed.";
	}

	//	public synchronized void saveAccounts() throws IOException {
	//		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("accounts.ser"));
	//		oos.writeObject(accounts);
	//		oos.close();
	//	}

}

