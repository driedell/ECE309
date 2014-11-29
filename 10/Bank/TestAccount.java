
public class TestAccount {
	public static void main(String[] args) throws Exception {
		CheckingAccount a = new CheckingAccount();
		a.setCustomerName("Bubba Smith");
		a.deposit(100);
		a.chargeFee(10);
		System.out.println(a.getCustomerName() + " "
				+ a.getAccountNumber()+ " $"
				+ a.getBalance());

		SavingsAccount a2 = new SavingsAccount("Mary Jones");
		a2.deposit(100);
		a2.withdraw(25);
		a2.addInterest(10);
		System.out.println(a2); // println() calls toString() on a2
	}
}
