// David Riedell dariedel@ncsu.edu

public class NewsConference {
	private String presidentsStatement = "";

	public synchronized void makeAstatement(String statement) {	// back door
		this.presidentsStatement = statement.toUpperCase();
		notifyAll();
	}

	public synchronized String joinTheNewsConference(int reporterNumber, String topic) {	// front door
		String myTopic = topic.toUpperCase();

		while(true) {
			if (this.presidentsStatement.contains(myTopic)) {
				return this.presidentsStatement;
			}
			if (this.presidentsStatement.contains("GOD BLESS AMERICA")) {
				return "My topic '" + topic + "' was not covered in today's news conference.";
			}
			try	{
				wait();
			} catch (InterruptedException localInterruptedException) {}
		}
	}
}