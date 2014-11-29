//David Riedell dariedel@ncsu.edu

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.*;


public class PictureChatClient implements ActionListener, Runnable {

	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Provide network address of the ChatServer and your chat name and password as 3 command line parameters.");
			return;
		}
		
		try {
			new PictureChatClient(args[0], args[1], args[2]);
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
	}

String chatName;
Socket s;
ObjectInputStream  ois = null;
ObjectOutputStream oos = null;

// whosIn window GUI objects
JFrame whosInWindow = new JFrame(chatName + " - Who's IN the Chat Room");
JList<String> whosInList = new JList<String>();
JScrollPane whosInScrollPane = new JScrollPane(whosInList);

//whosNotIn window GUI objects
JFrame whosNotInWindow = new JFrame("Who's NOT IN the Chat Room");
JList<String> whosNotInList = new JList<String>();
JScrollPane whosNotInScrollPane = new JScrollPane(whosNotInList);

//Chat window GUI Objects
JFrame		chatWindow			= new JFrame();
JLabel		chatLabel			= new JLabel("Move bar to give more space to in or out chat area."); 
JButton		sendToAllButton		= new JButton("Send To All");	 
JTextArea	inChatArea			= new JTextArea();
JTextArea	outChatArea			= new JTextArea();
JScrollPane	inChatScrollPane	= new JScrollPane(inChatArea);
JScrollPane	outChatScrollPane	= new JScrollPane(outChatArea);
JSplitPane	chatPane			= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inChatScrollPane, outChatScrollPane);
JMenuBar	menuBar				= new JMenuBar();
JMenu		chatMenu			= new JMenu("ScreenOrientation");
JMenuItem	horizontalSplit		= new JMenuItem("horizontal");
JMenuItem	verticalSplit		= new JMenuItem("vertical");

//Privacy GUI objects
JButton clearWhosInButton		= new JButton("CLEAR");
JButton clearWhosNotInButton	= new JButton("CLEAR");
JButton sendPrivateButton		= new JButton("Send Private");
JButton saveMessageButton		= new JButton("Save Message");


	
	public PictureChatClient(String serverAddress, String chatName, String password) throws UnknownHostException, IOException, ClassNotFoundException {
		this.chatName = chatName;
		
		if ((serverAddress == null)	|| (serverAddress.trim().length() == 0)	|| serverAddress.trim().contains(" ")) {
			throw new IllegalArgumentException("serverAddress is null, zero length, or contains blank(s)");
		} else {
			serverAddress = serverAddress.trim();
		}
	
		if ((chatName == null)	|| (chatName.trim().length() == 0)	|| chatName.trim().contains(" ")) {
			throw new IllegalArgumentException("chatName is null, zero length, or contains blank(s)");
		} else {
			chatName = chatName.trim();
		}
		
		if ((password == null)	|| (password.trim().length() == 0)	|| password.trim().contains(" ")) {
			throw new IllegalArgumentException("password is null, zero length, or contains blank(s)");
		} else {
			password = password.trim();
		}
		
		System.out.println("Connecting to ChatServer at " + serverAddress + " on port 5555");
		s = new Socket(serverAddress, 5555);
		System.out.println("Connected to ChatServer!");
		
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(chatName + "/" + password);
		ois = new ObjectInputStream(s.getInputStream());
		String serverReply = (String) ois.readObject();

		if(serverReply.startsWith("Welcome")) {
			System.out.println(serverReply);
			new Thread(this).start();	//make application thread
		} else {
			throw new IllegalArgumentException(serverReply);
		}
		
		// Build GUIs
		whosInWindow.getContentPane().add(whosInScrollPane,"Center");
		whosInWindow.getContentPane().add(clearWhosInButton,"North");
		whosInWindow.getContentPane().add(sendPrivateButton,"South");
		whosInWindow.setSize(200,200);
		whosInWindow.setLocation(0,0);
		whosInWindow.setVisible(true);
		whosInWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		whosNotInWindow.getContentPane().add(whosNotInScrollPane,"Center");
		whosNotInWindow.getContentPane().add(clearWhosNotInButton,"North");
		whosNotInWindow.getContentPane().add(saveMessageButton,"South");
		whosNotInWindow.setSize(200,200);
		whosNotInWindow.setLocation(200,0);
		whosNotInWindow.setVisible(true);
		whosNotInWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		chatWindow.getContentPane().add(chatLabel,"North");
		chatWindow.getContentPane().add(chatPane, "Center");
		chatWindow.getContentPane().add(sendToAllButton, "South");
		chatWindow.setTitle(chatName + "'s ChatRoom!");
		chatWindow.setSize(600,600);
		chatWindow.setLocation(0,200);
		chatWindow.setVisible(true);
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		outChatArea.setEditable(false); // keep cursor out
		inChatArea.setFont (new Font("default",Font.BOLD,20));
		outChatArea.setFont(new Font("default",Font.BOLD,20));
		inChatArea.setLineWrap(true);
		outChatArea.setLineWrap(true);
		inChatArea.setWrapStyleWord(true);
		outChatArea.setWrapStyleWord(true);
		inChatArea.requestFocus();
		sendToAllButton.setBackground(Color.GREEN);
		sendToAllButton.setForeground(Color.WHITE);
		chatWindow.setFont(new Font("default", Font.BOLD, 20));
		sendPrivateButton.setBackground(Color.RED);
		sendPrivateButton.setForeground(Color.WHITE);
		saveMessageButton.setBackground(Color.BLUE);
		saveMessageButton.setForeground(Color.WHITE);
		
		// Activate GUI objects for events 
		sendToAllButton.addActionListener(this);//give it our address
		sendToAllButton.setMnemonic(KeyEvent.VK_ENTER);//same as pushing button
		clearWhosInButton.addActionListener(this);
		clearWhosNotInButton.addActionListener(this);
		sendPrivateButton.addActionListener(this);
		saveMessageButton.addActionListener(this);

		

		// Build and show menu
		chatMenu.add(horizontalSplit);
		chatMenu.add(verticalSplit);
		menuBar.add(chatMenu);
		chatWindow.setJMenuBar(menuBar);
	    horizontalSplit.addActionListener(this);
	    verticalSplit.addActionListener(this);

	}

	@Override
	public void run() {
		try {
			while(true) {
				Object somethingFromTheServer = ois.readObject(); // wait for server to send	
				if(somethingFromTheServer instanceof String[]) {
					String[] clientArray = (String[]) somethingFromTheServer;
					System.out.println(Arrays.toString(clientArray));
										
					if(Arrays.binarySearch(clientArray, chatName.toUpperCase()) >= 0) {
						whosInList.setListData(clientArray);
					} else {
						whosNotInList.setListData(clientArray);
					}
				} else if(somethingFromTheServer instanceof String) {
					outChatArea.append("\n" + somethingFromTheServer.toString());
					outChatArea.setCaretPosition(outChatArea.getDocument().getLength());
					inChatArea.requestFocus();
				} else {
					System.out.println("Received from server: " + somethingFromTheServer);					
				}
			}
		} catch (ClassNotFoundException cfne) {
			
		} catch (IOException ioe) {
			System.out.println("Failure to receive connection.");
			return;
		}
		

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		chatLabel.setText("");	// clear

		if (ae.getSource() == sendToAllButton) {
			String chatToSend = inChatArea.getText().trim();
			if (chatToSend.length() == 0) {
				chatLabel.setForeground(Color.red);
				chatLabel.setText("NO MESSAGE TO SEND!");
				return;
			}
			inChatArea.setText(""); // clear input area on GUI
			System.out.println("Sending " + chatToSend);
			
			try {
				oos.writeObject(chatToSend);
			} catch (IOException e) {
				return;
			}
		}
		
		if (ae.getSource() == sendPrivateButton) {
			List<String> recipients = whosInList.getSelectedValuesList();
			if(recipients.isEmpty()) {
				chatLabel.setForeground(Color.RED);
				chatLabel.setText("NO RECIPIENTS ARE SELECTED");
				return;
			}
			String chatMessage = inChatArea.getText().trim();
			inChatArea.setText("");
			if (chatMessage.length() == 0) {
				chatLabel.setForeground(Color.RED);
				chatLabel.setText("NO MESSAGE TO SEND!");
				return;
			}
			
			String sendPrivateMessage = chatMessage + recipients;
			
			try {
				oos.writeObject(sendPrivateMessage);
			} catch (IOException e) {
			}
			
			System.out.println("Sending " + chatMessage + " to private recipients: " + recipients);
			return;
		}
		if (ae.getSource() == saveMessageButton) {
			List<String> recipients = whosNotInList.getSelectedValuesList();
			if(recipients.isEmpty()) {
				chatLabel.setForeground(Color.RED);
				chatLabel.setText("NO RECIPIENTS ARE SELECTED");
				return;
			}
			String savedMessage = inChatArea.getText().trim();
			inChatArea.setText("");
			if (savedMessage.length() == 0) {
				chatLabel.setForeground(Color.RED);
				chatLabel.setText("NO MESSAGE TO SEND!");
				return;
			}
			
			String saveMessageMessage = savedMessage + recipients;
			
			try {
				oos.writeObject(saveMessageMessage);
			} catch (IOException e) {
			}
			
			System.out.println("Saving " + savedMessage + " for private recipients: " + recipients);
			return;
		}
		if (ae.getSource() == clearWhosInButton) {
			whosInList.clearSelection();
			return;
		}
		if (ae.getSource() == clearWhosNotInButton) {
			whosNotInList.clearSelection();
			return;
		}

		
		if (ae.getSource() == horizontalSplit) {
			chatPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			return;
		}
		
		if (ae.getSource() == verticalSplit) {
			chatPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			return;
		}
	}
}
