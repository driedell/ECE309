// David Riedell dariedel@ncsu.edu
import java.io.*;
import java.util.*;

public class Therapist {
	public static void main(String[] args) throws IOException {
		System.out.println("Author: David Riedell dariedel@ncsu.edu\n");
		System.out.println("Title: On Line Therapy System\n");
		System.out.println("Ask me a Yes or No question. (Type EXIT to terminate the therapy session.)\n");

		// Load I/O classes
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader     br = new BufferedReader(isr);

		String[] answers = {"Yes.","No.","HELL NO!","Definitely!","Does a bear crap in the woods?","Nope.","Absolutely!","Uh... no.","I don't think so...","Probably.","...Duh.","YEAAAAAHHHH!!!"};
			// index		 0      1     2          3             4                               5        6             7           8                     9           10        11

		FileWriter         fw = new FileWriter("TherapyLog.txt", true);
		BufferedWriter     bw = new BufferedWriter(fw);

		Date sessionDate = new Date();
		bw.write(sessionDate.toString());
		bw.newLine();

		while(true) {
			String keyboardInput = br.readLine();
			if (keyboardInput.trim().length() == 0) continue;
			if (keyboardInput.equals("EXIT")) break;
			int index = (int) (Math.random() * answers.length);
			
			System.out.println(answers[index] + " ");
			
			String answer = answers[index];
			
			bw.write("Question was: \"" + keyboardInput + "\", Therapist answer was: \"" + answer + "\"");
			bw.newLine();
		}
	bw.close();
	}
}