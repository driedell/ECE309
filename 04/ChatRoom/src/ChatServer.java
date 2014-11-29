//David Riedell dariedel@ncsu.edu

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class ChatServer implements Runnable {

	public static void main(String[] args) {
		
		if(args.length != 0) {
			System.out.println("FYI ChatServer does not accept command line parameters.");
		}
		
		try {
			new ChatServer();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}

ServerSocket ss;	
int threadNumber = 1;
DataInputStream dis = null;
DataOutputStream dos = null;
String chatName;
String password;
ConcurrentHashMap<String, DataOutputStream> whosIn = new ConcurrentHashMap<String, DataOutputStream>();
ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>();


	public ChatServer() throws IOException {
		ss = new ServerSocket(4444);				
		System.out.println("ChatServer is up at " + InetAddress.getLocalHost().getHostAddress() + " on port " + ss.getLocalPort());
		new Thread(this).start();
		
		// Restore passwords collection from disk
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Passwords.ser"));
			passwords = (ConcurrentHashMap<String, String>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Passwords.ser was not found, so we will create a new one.");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Application thread #" + threadNumber++ + " entering run() to wait for a client to connect.");
		
		try {
			Socket s = ss.accept();
			new Thread(this).start();	// make another app thread
			System.out.println("A client has connected from " + s.getInetAddress());
			
			dis = new DataInputStream(s.getInputStream());
			String firstMessage = dis.readUTF();
			dos = new DataOutputStream(s.getOutputStream());
			
			if(firstMessage.contains("/") == true) {
				int slashOffset = firstMessage.indexOf("/");
				chatName = firstMessage.substring(0, slashOffset);
				password = firstMessage.substring(slashOffset+1, firstMessage.length());
				System.out.println(chatName + " is attempting to join with password " + password);
			} else {
				dos.writeUTF("Sorry - wrong number!");
				dos.close();		// "flush" out the message, then hang up
				return;				// and kill this client thread
			}
			
			chatName = chatName.toUpperCase();
			
			
			if(whosIn.containsKey(chatName)) {
				dos.writeUTF("Duplicate chat name. Please rejoin with a different name.");
				dos.close();		// "flush" out the message, then hang up
				return;				// and kill this client thread
			} else {
				whosIn.put(chatName, dos);		// add a client and their dos
			}
//			DataOutputStream clientDOS = whosIn.get(chatName);
			
			
			if(passwords.containsKey(chatName)) {
				
			} else {				// new user wants to join
				passwords.put(chatName, password);
				savePasswords();	// on disk
			}
			
			dos.writeUTF("Welcome " + chatName);
			
			String storedPassword = passwords.get(chatName);
			if(!password.equals(storedPassword)) {
				dos.writeUTF("Entered password does not match stored password.");
				dos.close();		// "flush" out the message, then hang up
				return;				// and kill this client thread
			}
			
		} catch (IOException e) {
			System.out.println("A client connection attempt has failed.");
			return;
		}
		
		
	}

	private synchronized void savePasswords() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Passwords.ser"));
		oos.writeObject(passwords);
		oos.close();
	}



}
