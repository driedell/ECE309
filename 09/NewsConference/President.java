// David Riedell dariedel@ncsu.edu

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class President implements Runnable {
	NewsConference newsConference;

	public President(NewsConference newsConference) {
		this.newsConference = newsConference;
		new Thread(this).start();
	}

	public void run() {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String newLine = System.lineSeparator();
		System.out.println("The news conference has started!");
		System.out.println("As the President of the United States, " + 
				newLine + "enter various comments that you think might be of interest." + 
				newLine + "If you mention a topic that a reporter is interested in," + 
				newLine + "you will see them leave the news conference with the story.");

		while(true) {
			try {
				String statement;
				do {
					statement = keyboard.readLine().toUpperCase();
					this.newsConference.makeAstatement(statement);
				} while (!statement.contains("GOD BLESS AMERICA"));
				System.out.println("News Conference is terminated.");
				return;
			} catch (IOException ioe) {
				System.out.println("There was an error.");
			}
		}
	}
}