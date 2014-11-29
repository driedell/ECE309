import java.rmi.Remote;
import java.rmi.RemoteException;


public interface TellerInterface extends Remote {
	String openNewAccount(String accountType, String  customerName) throws RemoteException;
	String processAccount(String processType, int accountNumber, double  amount) throws RemoteException;
	String showAccount(int accountNumber) throws RemoteException;
	String showAccount(String customerName) throws RemoteException;
	String closeAccount(int accountNumber, String customerName) throws RemoteException;
}
