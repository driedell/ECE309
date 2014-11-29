// David Riedell dariedel@ncsu.edu

import java.io.PrintStream;

public class Reporter implements Runnable {
	private int reporterNumber;
	private NewsConference ncAddress;
	private String topicOfInterest;

	public Reporter(int reporterNumber, NewsConference ncAddress, String topicOfInterest) {
		this.reporterNumber = reporterNumber;
		this.ncAddress = ncAddress;
		this.topicOfInterest = topicOfInterest;
		new Thread(this).start();
	}

	public void run() {
		String statement = this.ncAddress.joinTheNewsConference(this.reporterNumber, this.topicOfInterest);
		System.out.println("Reporter #" + this.reporterNumber + " is returning from the news conference.");
		System.out.println("My topic was " + this.topicOfInterest);
		System.out.println("The presiden't statament was: " + statement);
	}
}