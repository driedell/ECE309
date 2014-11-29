// David Riedell, dariedel@ncsu.edu

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public class StarterTellerClient {

	public StarterTellerClient(String serverAddress) throws RemoteException {
		TellerInterface server;		
		String serverURL = "rmi://" + serverAddress + "/TellerServices";
		try {
			server = (TellerInterface) Naming.lookup(serverURL);
		} catch(MalformedURLException murl) {
		      System.out.println("Entered server address FORMAT is invalid.");
		      return;
		} catch(NotBoundException nb) {
		      System.out.println("If the server address is correct, the TellerServices application is not up.");
		      return;
		} catch(RemoteException re) {
			  throw new RemoteException("Connection Error. "
		              + "If the entered server address "
		              + "is correct and the network is up, "
		              + "then the server is off-line.",re);
		}
		 
		// Do test calls to the server methods.
		System.out.println(server.openNewAccount("Savings","David"));
		System.out.println(server.processAccount("Deposit",12345,50));
		System.out.println(server.showAccount("David"));
		System.out.println(server.showAccount(12345));
		System.out.println(server.closeAccount(12345,"David"));

	}

	public static void main(String[] args) throws RemoteException {
		if(args.length != 1) {
			System.out.println("Please enter the ServerAddress");
			return;
		}
		new StarterTellerClient(args[0]);

	}

}
