// David Riedell dariedel@ncsu.edu
import java.io.*;
import java.util.*;
import java.net.*;

public class TherapistServer {
	public static void main(String[] args) throws IOException {
		System.out.println("Author: David Riedell dariedel@ncsu.edu");
		System.out.println("Program: TherapistServer\n");
		
		if(args.length >= 1) {
			System.out.println("The server doesn't accept command line parameters.");
		}
		
		
		ServerSocket ss = new ServerSocket(2222);		// port 2222
		String serverAddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println(serverAddress + ", " + ss + "\n");

		String[] answers = {"Yes.","No.","HELL NO!","Definitely!","Does a bear crap in the woods?","Nope.","Absolutely!","Uh... no.","I don't think so...","Probably.","...Duh.","YEAAAAAHHHH!!!"};
			// index		 0      1     2          3             4                               5        6             7           8                     9           10        11


		while(true) {
			try {
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String message = dis.readUTF();		//wait for send
				
				int index = (int) (Math.random() * answers.length);
				
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(answers[index]);
				
				s.close();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}
}