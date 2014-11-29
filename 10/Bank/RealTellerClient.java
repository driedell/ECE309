// David Riedell, dariedel@ncsu.edu


//import java.net.MalformedURLException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.Remote;
//import java.rmi.RemoteException;
//
//
//public class RealTellerClient {
//
//	public RealTellerClient(String serverAddress) throws RemoteException {
//		TellerInterface server;		
//		String serverURL = "rmi://" + serverAddress + "/TellerServices";
//		try {
//			server = (TellerInterface) Naming.lookup(serverURL);
//		} catch(MalformedURLException murl) {
//			System.out.println("Entered server address FORMAT is invalid.");
//			return;
//		} catch(NotBoundException nb) {
//			System.out.println("If the server address is correct, the TellerServices application is not up.");
//			return;
//		} catch(RemoteException re) {
//			throw new RemoteException("Connection Error. "
//					+ "If the entered server address "
//					+ "is correct and the network is up, "
//					+ "then the server is off-line.",re);
//		}
//
//		// Do test calls to the server methods.
//		System.out.println(server.openNewAccount("Savings","David"));
//		System.out.println(server.processAccount("Deposit",12345,50));
//		System.out.println(server.showAccount("David"));
//		System.out.println(server.showAccount(12345));
//		System.out.println(server.closeAccount(12345,"David"));
//
//	}
//
//	public static void main(String[] args) throws RemoteException {
//		if(args.length != 1) {
//			System.out.println("Please enter the ServerAddress");
//			return;
//		}
//		new RealTellerClient(args[0]);
//
//	}
//
//}




import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class RealTellerClient implements ActionListener {
	public static void main(String[] args) {
		String newLine = System.lineSeparator();
		if(args.length != 2) {
			System.out.println("Server address must be provided as command line parameter #1" + 
					newLine + "and the server application name as command line parameter #2");
			return;
		}
		String serverAddress = args[0];
		String serverAppName = args[1];
		try {
			new RealTellerClient(serverAddress, serverAppName);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private JFrame window = new JFrame("BANK TELLER");
	private JTextField accountTextField = new JTextField(8);
	private JTextField amountTextField = new JTextField(8);
	private JTextField customerNameTextField = new JTextField(8);
	private JLabel accountLabel = new JLabel("Enter account #");
	private JLabel amountLabel = new JLabel("Enter $ amount");
	private JLabel customerNameLabel = new JLabel("Enter customer name Last,First");
	private JButton closeButton = new JButton("Close");
	private JButton clearButton = new JButton("Clear");
	private JButton showByNumberButton = new JButton("Show Account");
	private JButton showByNameButton = new JButton("Show All Accounts");
	private JButton depositButton = new JButton("Deposit");
	private JButton withdrawButton = new JButton("Withdraw");
	private JButton openNewCheckingButton = new JButton("New Checking");
	private JButton openNewSavingsButton = new JButton("New Savings");
	private JTextArea displayTextArea = new JTextArea();
	private JScrollPane displayScrollPane = new JScrollPane(this.displayTextArea);
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	TellerInterface server = null;

	public RealTellerClient(String serverAddress, String serverAppName) throws Exception {
		this.topPanel.add(this.accountLabel);
		this.topPanel.add(this.accountTextField);
		this.topPanel.add(this.showByNumberButton);
		this.topPanel.add(this.amountLabel);
		this.topPanel.add(this.amountTextField);
		this.topPanel.add(this.depositButton);
		this.topPanel.add(this.withdrawButton);
		this.topPanel.add(this.closeButton);

		this.window.getContentPane().add(this.topPanel, "North");

		this.window.getContentPane().add(this.displayScrollPane, "Center");

		this.bottomPanel.add(this.clearButton);
		this.bottomPanel.add(this.customerNameLabel);
		this.bottomPanel.add(this.customerNameTextField);
		this.bottomPanel.add(this.showByNameButton);
		this.bottomPanel.add(this.openNewCheckingButton);
		this.bottomPanel.add(this.openNewSavingsButton);
		this.window.getContentPane().add(this.bottomPanel, "South");

		this.displayTextArea.setEditable(false);
		this.displayTextArea.setFont(new Font("default", 1, 20));
		this.showByNumberButton.setBackground(Color.black);
		this.showByNameButton.setBackground(Color.black);
		this.showByNumberButton.setForeground(Color.yellow);
		this.showByNameButton.setForeground(Color.yellow);
		this.depositButton.setBackground(Color.green);
		this.withdrawButton.setBackground(Color.green);
		this.clearButton.setBackground(Color.yellow);
		this.closeButton.setBackground(Color.red);
		this.openNewCheckingButton.setBackground(Color.cyan);
		this.openNewSavingsButton.setBackground(Color.cyan);

		this.showByNumberButton.addActionListener(this);
		this.showByNameButton.addActionListener(this);
		this.openNewCheckingButton.addActionListener(this);
		this.openNewSavingsButton.addActionListener(this);
		this.depositButton.addActionListener(this);
		this.withdrawButton.addActionListener(this);
		this.clearButton.addActionListener(this);
		this.closeButton.addActionListener(this);

		this.window.setLocation(0, 0);
		this.window.setSize(1000, 400);
		this.window.setDefaultCloseOperation(3);
		this.window.setVisible(true);

		try {
			this.server = ((TellerInterface)Naming.lookup("rmi://" + serverAddress + "/" + serverAppName));
			System.out.println("Connected to " + serverAppName + " at " + serverAddress);
			this.displayTextArea.setText("Connected to bank server at " + serverAddress + " with app name " + serverAppName);
		} catch (Exception e) {
			this.displayTextArea.setText("Problems connecting to bank server at " + serverAddress + " with app name " + serverAppName + " " + e);
			throw e;
		}
	}

	public void actionPerformed(ActionEvent ae) {
		this.displayTextArea.setText("");
		if(ae.getSource() == this.clearButton) {		// Clear button
			this.accountTextField.setText("");
			this.amountTextField.setText("");
			this.customerNameTextField.setText("");
			this.displayTextArea.setText("");
			return;
		}
		try {
			if(ae.getSource() == this.closeButton) {
				processAccount(Bank.CLOSE);
			} else if(ae.getSource() == this.depositButton) {
				processAccount(Bank.DEPOSIT);
			} else if(ae.getSource() == this.withdrawButton) {
				processAccount(Bank.WITHDRAW);
			} else if(ae.getSource() == this.openNewCheckingButton) {
				openNewAccount(Bank.CHECKING);
			} else if(ae.getSource() == this.openNewSavingsButton) {
				openNewAccount(Bank.SAVINGS);
			} else if(ae.getSource() == this.showByNumberButton) {
				showAccount(Bank.SHOW_BY_NUMBER);
			} else if(ae.getSource() == this.showByNameButton) {
				showAccount(Bank.SHOW_BY_NAME);
			} else {
				this.displayTextArea.setText("PROGRAM ERROR: " + ae.getSource() + " not recognized in actionPerformed() in TellerClient.");
			}
		} catch (IllegalArgumentException iae) {
			this.displayTextArea.setText(iae.getMessage());
		} catch (RemoteException re) {
			this.displayTextArea.setText(re.toString());
		}
	}


	private void processAccount(String processType) throws IllegalArgumentException, RemoteException {
		if (processType.equals(Bank.DEPOSIT) || processType.equals(Bank.WITHDRAW)) {
			System.out.println("In processAccount() doing a " + processType);
			// Check the three text fields:
			/*1*/ int    accountNumber = getAccountNumber();
			/*2*/ double amount        = getAmount();
			/*3*/ notCustomerName(processType);
			displayTextArea.setText(server.processAccount(processType,accountNumber,amount));
		}
		if(processType.equals(Bank.CLOSE)) {
			System.out.println("In processAccount() doing a close.");
			int    accountNumber = getAccountNumber();
			String customerName  = getCustomerName();
			notAmount(processType); // and NOT an amount! 
			this.displayTextArea.setText(this.server.closeAccount(accountNumber, customerName) + " is being closed.");
		}
	}

	private void openNewAccount(String accountType) throws IllegalArgumentException, RemoteException {
		String customerName = getCustomerName();
		notAccount("openNewAccount"); // but NOT account#
		notAmount("openNewAccount");  // or an amount !
		if(accountType.equals("CHECKING")) {
			System.out.println("newChecking button was pushed");
			this.displayTextArea.setText(this.server.openNewAccount(accountType, customerName));
		}
		if(accountType.equals("SAVINGS")) {
			System.out.println("newSavings button was pushed");
			this.displayTextArea.setText(this.server.openNewAccount(accountType, customerName));
		}
	}

	private void showAccount(String showType) throws IllegalArgumentException, RemoteException {
		notAmount(showType);
		if(showType.equals("SHOW_BY_NUMBER")) {
			System.out.println("Show Account By Number button was pushed");
			notCustomerName(showType);
			int accountNumber = getAccountNumber();
			this.displayTextArea.setText(this.server.showAccount(accountNumber));
		}
		if(showType.equals("SHOW_BY_NAME")) {
			System.out.println("showAccountsNyName button was pushed");
			notAccount(showType);
			String customerName = this.customerNameTextField.getText().trim();
			if(customerName.length() == 0) {
				throw new IllegalArgumentException("Customer name required (may be a 'short' name).");
			}
			this.displayTextArea.setText(this.server.showAccount(customerName));
		}
	}

	private String getCustomerName() throws IllegalArgumentException {
		System.out.println("getCustomerName() method was called");

		String customerName = this.customerNameTextField.getText().trim();
		if(customerName.length() == 0) {
			throw new IllegalArgumentException("Customer name is required.");
		}
		if(!customerName.contains(",")) {
			throw new IllegalArgumentException("Customer name must be entered in form Last,First");
		}
		if((customerName.startsWith(",")) || (customerName.endsWith(","))) {
			throw new IllegalArgumentException("Customer name must not start or end with a comma.");
		}
		int commaOffset = customerName.indexOf(",");
		int nextCommaOffset = customerName.indexOf(",", commaOffset + 1);
		if(nextCommaOffset != -1) {
			throw new IllegalArgumentException("Customer name must not contain more than one comma.");
		}
		return customerName;
	}

	private int getAccountNumber() throws IllegalArgumentException {
		System.out.println("getAccountNumber() method was called");

		String accountNumber = this.accountTextField.getText().trim();
		if(accountNumber.length() == 0) {
			throw new IllegalArgumentException("Account number required.");
		}
		int account = 0;
		try {
			account = Integer.parseInt(accountNumber);
			if(account < 1) {
				throw new IllegalArgumentException("Account number must be an integer value > 0.");
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Account number must be an integer value > 0.");
		}
		return account;
	}

	private double getAmount() throws IllegalArgumentException {
		System.out.println("getAmount() method was called");

		String amountString = this.amountTextField.getText().trim();
		if(amountString.length() == 0) {
			throw new IllegalArgumentException("Amount is required.");
		}
		if(amountString.startsWith("0")) {
			throw new IllegalArgumentException("Amount must not have a leading 0");
		}
		if(amountString.startsWith("$")) {
			amountString = amountString.substring(1).trim();
		}
		if(amountString.contains(",")) {
			throw new IllegalArgumentException("Amount must not contain commas.");
		}
		int decimalOffset = amountString.indexOf(".");
		if(decimalOffset > -1) {
			if(amountString.length() != decimalOffset + 3) {
				throw new IllegalArgumentException("If a decimal point is present in the amount it must be followed by 2 decimal digits.");
			}
		}
		double amount = 0;
		try {
			amount = Double.parseDouble(amountString);
			if(amount <= 0.0D) {
				throw new IllegalArgumentException("Amount must be positive.");
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Account number must be a whole number.");
		}
		return amount;
	}

	private void notAmount(String whatFunction) throws IllegalArgumentException {
		String amount = this.amountTextField.getText().trim();
		if(amount.length() == 0) {
			return;
		}
		throw new IllegalArgumentException("An amount should not be provided for " + whatFunction);
	}

	private void notAccount(String whatFunction) throws IllegalArgumentException {
		String account = this.accountTextField.getText().trim();
		if(account.length() == 0) {
			return;
		}
		throw new IllegalArgumentException("Account number should not be provided for " + whatFunction);
	}

	private void notCustomerName(String whatFunction) throws IllegalArgumentException {
		String customerName = this.customerNameTextField.getText().trim();
		if(customerName.length() == 0) {
			return;
		}
		throw new IllegalArgumentException("Customer name should not be provided for " + whatFunction);
	}
}