// David Riedell, dariedel@ncsu.edu

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TellerServer extends UnicastRemoteObject implements	TellerInterface {

	public static void main(String[] args) {
		
		try {
			new TellerServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TellerServer() throws Exception {
		super();
		Naming.rebind("TellerServices", this);
		System.out.println("TellerServices server is up!");   
	}
	
	@Override
	public String openNewAccount(String accountType, String customerName) {
		String statusString0 =  "Account type is: " + accountType + ", customer name is: " + customerName;

		return statusString0;
	}

	@Override
	public String processAccount(String processType, int accountNumber, double amount) {
		String statusString1 =  "Process type:  " + processType + ", Amount: $" + amount + ", Account number: " + accountNumber;

		return statusString1;
	}

	@Override
	public String showAccount(int accountNumber) {
		String statusString2 = "Account number: " + accountNumber;
		return statusString2;
	}

	@Override
	public String showAccount(String customerName) {
		String statusString3 = "Customer name: " + customerName;
		return statusString3;
	}

	@Override
	public String closeAccount(int accountNumber, String customerName) {
		String statusString4 = "Account number: " + accountNumber + ", Customer name: " + customerName;
		return statusString4;
	}


}
