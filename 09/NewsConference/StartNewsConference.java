// David Riedell dariedel@ncsu.edu

public class StartNewsConference
{
	public static void main(String[] args)
	{
		System.out.println("This program loads the NewsConference, the President, and multiple Reporter(s)");	
		if (args.length == 0) {
			System.out.println("Enter a command line parameter which identifies each Reporter's topic of interest."); 
			return; 
		}
		NewsConference newsConference = new NewsConference();

		int reporterNumber = 0;
		for (String topic : args) {
			reporterNumber++;
			Reporter reporter = new Reporter(reporterNumber, newsConference, topic);
			System.out.println("Reporter #" + reporterNumber + " will cover " + topic);
		}

		President president = new President(newsConference);
	}
}
