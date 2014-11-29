//David Riedell dariedel@ncsu.edu

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class PrivateChatServer implements Runnable {

	public static void main(String[] args) {
		
		if(args.length != 0) {
			System.out.println("FYI ChatServer does not accept command line parameters.");
		}
		
		try {
			new PrivateChatServer();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}

ServerSocket ss;	
int threadNumber = 1;
ConcurrentHashMap<String, ObjectOutputStream> whosIn = new ConcurrentHashMap<String, ObjectOutputStream>();
ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>();
Vector<String> whosNotIn;

	public PrivateChatServer() throws IOException {
		ss = new ServerSocket(5555);				
		System.out.println("ChatServer is up at " + InetAddress.getLocalHost().getHostAddress() + " on port " + ss.getLocalPort());
		new Thread(this).start();
		
		// Restore passwords collection from disk
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Passwords.ser"));
			passwords = (ConcurrentHashMap<String, String>) ois.readObject();
			Set notInSet = passwords.keySet();
			whosNotIn = new Vector<String>(notInSet);
			ois.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Passwords.ser was not found, so we will create a new one.");
//			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Application thread #" + threadNumber++ + " entering run() to wait for a client to connect.");
		
	Socket s;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	String chatName;
	String password;
		
// JOIN Processing
		try {
			s = ss.accept();
			new Thread(this).start();	// make another app thread
			System.out.println("A client has connected from " + s.getInetAddress());
			
		    ois = new ObjectInputStream(s.getInputStream());
		    String firstMessage = (String) ois.readObject();
		    oos = new ObjectOutputStream(s.getOutputStream());
			
			if(firstMessage.contains("/") == true) {
				int slashOffset = firstMessage.indexOf("/");
				chatName = firstMessage.substring(0, slashOffset);
				password = firstMessage.substring(slashOffset+1, firstMessage.length());
				System.out.println(chatName + " is attempting to join with password " + password);
			} else {
			    oos.writeObject("Sorry - wrong number!");
			    oos.close(); 		// "flush" out the msg, then hang up.
				return;				// and kill this client thread
			}
			
			chatName = chatName.toUpperCase();
			
			if(whosIn.containsKey(chatName)) {
				oos.writeObject("Duplicate chat name. Please rejoin with a different name.");
				oos.close();		// "flush" out the message, then hang up
				return;				// and kill this client thread
			} else {
			    whosIn.put(chatName, oos);
			}
//			ObjectOutputStream clientDOS = whosIn.get(chatName);
			
			
			if(passwords.containsKey(chatName)) {
			} else {				// new user wants to join
				passwords.put(chatName, password);
				savePasswords();	// on disk
			}
			
			oos.writeObject("Welcome " + chatName);
			
			String storedPassword = passwords.get(chatName);
			if(!password.equals(storedPassword)) {
				oos.writeObject("Entered password does not match stored password.");
				oos.close();		// "flush" out the message, then hang up
				return;				// and kill this client thread
			}
			
			// send whosIn list to new client
			Set<String> keyList = whosIn.keySet();
			String[] whosInArray = whosIn.keySet().toArray(new String[0]);
			Arrays.sort(whosInArray);
			sendToAll(whosInArray);
			
			// send whosNotIn list to new client
			whosNotIn.remove(chatName);
			String[] whosNotInArray = whosNotIn.toArray(new String[0]);
			Arrays.sort(whosNotInArray);
			sendToAll(whosNotInArray);
			
			sendToAll("Welcome to " + chatName + " who has just joined the chat room.");
			

		
// RECEIVE Processing
			try {
				while(true) {
					String message = (String) ois.readObject();
					sendToAll(chatName + " says: " + message);
					System.out.println("Received '" + message + "' from " + chatName);
				} 
			} catch(IOException ioe) {
// LEAVE Processing
				whosIn.remove(chatName);
//				String[] whosInArray = whosIn.keySet().toArray(new String[0]);
				Arrays.sort(whosInArray);
				sendToAll(whosInArray);
				
				whosNotIn.add(chatName);
//				String[] whosNotInArray = whosNotIn.toArray(new String[0]);
				Arrays.sort(whosNotInArray);
				sendToAll(whosNotInArray);
				
				sendToAll(chatName + " has left the chat room.");
//				oos.writeObject("Client " + chatName + " has left.");
				
			}
		} catch (ClassNotFoundException cfne) {
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
	
	private void sendToAll(Object objectToSend) {
		ObjectOutputStream[] oosList = whosIn.values().toArray(new ObjectOutputStream[0]);
		for (ObjectOutputStream clientOOS : oosList) {
			try {
				clientOOS.writeObject(objectToSend);
			} catch(IOException ioe){
			} // go on to next client
		}                       // (this one just left)  
	}
	
	private synchronized void sendPrivateMessage(String[] messageWithRecipients, String senderChatName) {
		
	}

	private synchronized void saveAmessage(String[] messageWithRecipients, String senderChatName) {
		
	}



}
