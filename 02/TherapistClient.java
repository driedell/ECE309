// David Riedell dariedel@ncsu.edu
import java.io.*;
import java.util.*;
import java.net.*;

public class TherapistClient {
	public static void main(String[] args) throws IOException {
		System.out.println("Author: David Riedell dariedel@ncsu.edu");
		System.out.println("Program: TherapistClient\n");
	
		if(args.length < 1) {
			System.out.println("Please include your network address.");
			return;
		} else if(args.length > 1) {
			System.out.println("Too many parameters entered.");
			return;
		}
		String serverAddress = args[0];
		
		System.out.println("Ask me a Yes or No question.\n(Type EXIT to terminate the therapy session.)\n");

		// Load I/O classes
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader     br = new BufferedReader(isr);

		FileWriter         fw = new FileWriter("TherapyLog.txt", true);
		BufferedWriter     bw = new BufferedWriter(fw);

		//Create file header for new Therapy session
		Date sessionDate = new Date();
		bw.newLine();
		bw.newLine();
		bw.write(sessionDate.toString());
		bw.newLine();

		try {
			while(true) {
				String keyboardInput = br.readLine();
				if (keyboardInput.trim().length() == 0) continue;
				if (keyboardInput.equals("EXIT")) break;
				
				// Open a socket
				Socket s = new Socket(serverAddress,2222);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(args[0]);			//send
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String reply = dis.readUTF();		//wait for reply
				
				System.out.println("Your Therapist says: \"" + reply + "\"");
								
				bw.write("Question was: \"" + keyboardInput + "\", Therapist answer was: \"" + reply + "\"");
				bw.newLine();
			}
		} catch(IOException ioe) {
			System.out.println("There was an error communicating with the Server. Please try again later.\nserverAddress = " + serverAddress);
			
			bw.write("Session was terminated due to a communication problem.");
		}
	bw.close();
	}
}