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
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class TellerServer extends UnicastRemoteObject implements TellerInterface {
	public static void main(String[] args) {

		try {
			new TellerServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	ConcurrentHashMap<Integer, CashAccount> accounts = new ConcurrentHashMap<Integer, CashAccount>();
	String newLine = System.lineSeparator();

	public TellerServer() throws Exception {
		super(); //call parent constructor first
		// Restore passwords collection from disk

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("accounts.ser"));
			accounts =  (ConcurrentHashMap<Integer, CashAccount>) ois.readObject();
			ois.close();
			System.out.println(accounts);
		} catch (FileNotFoundException fnfe) {
			System.out.println("accounts.ser is not found on disk, so we will be starting with an empty passwords collection.");

		}

		//super();
		Naming.rebind("TellerServices", this);
		System.out.println("TellerServices server is up!");
		System.out.println("TellerServices is up at: " + InetAddress.getLocalHost().getHostAddress());
		System.out.println(accounts);
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
		//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("accounts.ser"));

		try {
			saveAccounts();
		} catch (IOException e) {
			return "ERROR: blah blah blah" ;
		}
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
				saveAccounts();
			} catch (IOException e) {
				return "ERROR: bleh bleh bleh" ;		
			}
		}
		else if(processType.equals(Bank.WITHDRAW)) {
			ca.withdraw(amount);
			try {
				saveAccounts();
			} catch (IOException e) {
				return "ERROR: bluh bluh bluh" ;		
			}
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
			saveAccounts();
		} catch (IOException e) {
			return "ERROR message returned to teller client." ;
		}
		System.out.println(ca.getBalance());
		return "Account #" + ca.getAccountNumber() + " is being closed.";
	}

	public synchronized void saveAccounts() throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("accounts.ser"));
		oos.writeObject(accounts);
		oos.close();
	}


}
