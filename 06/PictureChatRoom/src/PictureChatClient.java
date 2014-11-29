//David Riedell dariedel@ncsu.edu

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class PictureChatClient implements ActionListener, ListSelectionListener, Runnable {

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

	Socket s;
	ObjectInputStream  ois = null;
	ObjectOutputStream oos = null;
	String chatName;
	String newLine = System.lineSeparator();
	File localDirectory = new File(System.getProperty("user.dir"));
	TreeSet pictureFiles = new TreeSet();
	Vector ImagesV = new Vector();
	Vector receivedPictures = new Vector();
	ImageIcon[] Images = new ImageIcon[50];

	//Who's-in window GUI objects
	JFrame  whosInWindow         = new JFrame("Who's IN (Hold Ctrl to select multiple)");
	JList<String> whosInList     = new JList<String>();
	JScrollPane whosInScrollPane = new JScrollPane(whosInList);
	JButton clearWhosInButton    = new JButton("CLEAR");
	JButton sendPrivateButton    = new JButton("Send Private");

	//Who's-not-in window GUI objects
	JFrame  whosNotInWindow      = new JFrame("Who's NOT in (Hold Ctrl to select multiple)");
	JList<String> whosNotInList  = new JList<String>();
	JScrollPane whosNotInScrollPane = new JScrollPane(whosNotInList);
	JButton clearWhosNotInButton = new JButton("CLEAR");
	JButton saveMessageButton    = new JButton("Save Message");

	//picturePreview GUI Objects
	JFrame picturePreviewWindow = new JFrame("Put pictures to send in " + localDirectory);
	JLabel myPictureWindowLabel = new JLabel("");
	JButton previewPicturesButton = new JButton("Preview Pictures");
	JList<ImageIcon> myPicturesList = new JList<ImageIcon>();
	JScrollPane myPicturesScrollPane = new JScrollPane(myPicturesList);
	JButton clearPicSelectButton = new JButton("Clear Selection");

	//pictureSelect GUI Objects
//	JFrame pictureSelectWindow = new JFrame("Picture Select Window");
//	JButton selectPicturesButton = new JButton("Select Pictures");
	
	//receivedPictures GUI Objects
	JFrame receivedPicturesWindow = new JFrame("Received Pictures Window");
	JList<ImageIcon> receivedPicturesList = new JList<ImageIcon>();
	JScrollPane receivedPicturesScrollPane = new JScrollPane(receivedPicturesList);
	JButton clearReceivedSelectButton = new JButton("Clear Received Pictures");
	JButton eraseReceivedSelectButton = new JButton("Erase Received Picture");
	JTextArea descriptionTextArea = new JTextArea("");
	JSplitPane picturesPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, receivedPicturesScrollPane, descriptionTextArea);
	JButton showReceivedPicturesButton = new JButton("Show Received Pictures");
	
	//Chat window GUI Objects
	JFrame		chatWindow				= new JFrame();
	JPanel		panel					= new JPanel();
	JLabel		chatLabel				= new JLabel("Move bar to give more space to in or out chat area."); 
	JButton		sendToAllButton			= new JButton("Send To All");
	JTextArea	inChatArea				= new JTextArea();
	JTextArea	outChatArea				= new JTextArea();
	JScrollPane	inChatScrollPane		= new JScrollPane(inChatArea);
	JScrollPane	outChatScrollPane		= new JScrollPane(outChatArea);
	JSplitPane	chatPane				= new JSplitPane(JSplitPane.VERTICAL_SPLIT, outChatScrollPane, inChatScrollPane);
	JMenuBar	menuBar					= new JMenuBar();
	JMenu		chatMenu				= new JMenu("ScreenOrientation");
	JMenuItem	horizontalSplit			= new JMenuItem("horizontal");
	JMenuItem	verticalSplit			= new JMenuItem("vertical");


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

		System.out.println("Connecting to ChatServer at " + serverAddress + " on port 6666");
		s = new Socket(serverAddress, 6666);
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
		whosInWindow.getContentPane().add(clearWhosInButton,"North");
		whosInWindow.getContentPane().add(whosInScrollPane,"Center");
		whosInWindow.getContentPane().add(sendPrivateButton,"South");
		whosInWindow.setSize(200,200);
		whosInWindow.setLocation(0,0);
		whosInWindow.setVisible(true);
		whosInWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		whosNotInWindow.getContentPane().add(clearWhosNotInButton,"North");
		whosNotInWindow.getContentPane().add(whosNotInScrollPane,"Center");
		whosNotInWindow.getContentPane().add(saveMessageButton,"South");
		whosNotInWindow.setSize(200,200);
		whosNotInWindow.setLocation(200,0);
		whosNotInWindow.setVisible(true);
		whosNotInWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		panel.add(sendToAllButton);
		panel.add(previewPicturesButton);
		panel.add(showReceivedPicturesButton);
//		panel.add(selectPicturesButton);
		chatWindow.getContentPane().add(chatLabel,"North");
		chatWindow.getContentPane().add(chatPane, "Center");
		chatWindow.getContentPane().add(panel, "South");
		//		chatWindow.getContentPane().add(sendToAllButton, "South");
		//		chatWindow.getContentPane().add(previewPicturesButton, "South");
		chatWindow.setTitle(chatName + "'s ChatRoom!");
		chatWindow.setSize(500,600);
		chatWindow.setLocation(0,200);
		chatWindow.setVisible(true);
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setFont(new Font("default", Font.BOLD, 20));

		outChatArea.setEditable(false); // keep cursor out
		inChatArea.setFont (new Font("default",Font.BOLD,20));
		outChatArea.setFont(new Font("default",Font.BOLD,20));
		inChatArea.setLineWrap(true);
		outChatArea.setLineWrap(true);
		inChatArea.setWrapStyleWord(true);
		outChatArea.setWrapStyleWord(true);
		inChatArea.requestFocus();
//		chatPane.setDividerLocation(0.75);
		sendToAllButton.setBackground(Color.GREEN);
		sendToAllButton.setForeground(Color.WHITE);
		sendPrivateButton.setBackground(Color.RED);
		sendPrivateButton.setForeground(Color.WHITE);
		saveMessageButton.setBackground(Color.BLUE);
		saveMessageButton.setForeground(Color.WHITE);

		// Build and show menu
		chatMenu.add(horizontalSplit);
		chatMenu.add(verticalSplit);
		menuBar.add(chatMenu);
		chatWindow.setJMenuBar(menuBar);

		// Activate GUI objects for events 
		sendToAllButton.addActionListener(this);//give it our address
		sendToAllButton.setMnemonic(KeyEvent.VK_ENTER);//same as pushing button
		clearWhosInButton.addActionListener(this);
		clearWhosNotInButton.addActionListener(this);
		sendPrivateButton.addActionListener(this);
		saveMessageButton.addActionListener(this);
		horizontalSplit.addActionListener(this);
		verticalSplit.addActionListener(this);
		previewPicturesButton.addActionListener(this);
//		selectPicturesButton.addActionListener(this);
		clearPicSelectButton.addActionListener(this);
		myPicturesList.addListSelectionListener(this);
		receivedPicturesList.addListSelectionListener(this);
		showReceivedPicturesButton.addActionListener(this);
		clearReceivedSelectButton.addActionListener(this);
		eraseReceivedSelectButton.addActionListener(this);

		//previewPictures GUI
		picturePreviewWindow.getContentPane().add(clearPicSelectButton, "North");
		picturePreviewWindow.getContentPane().add(myPicturesScrollPane, "Center");
		picturePreviewWindow.getContentPane().add(myPictureWindowLabel, "South");
		picturePreviewWindow.setSize(300, 300);
		picturePreviewWindow.setLocation(400, 0);
		picturePreviewWindow.setVisible(false);
		myPictureWindowLabel.setForeground(Color.RED);
		myPicturesList.setSelectionMode(0);
		
//receivedPicturesWindow GUI
		receivedPicturesWindow.getContentPane().add(clearReceivedSelectButton, "North");
		receivedPicturesWindow.getContentPane().add(picturesPane, "Center");
		receivedPicturesWindow.getContentPane().add(eraseReceivedSelectButton, "South");
		receivedPicturesWindow.setSize(300, 300);
		receivedPicturesWindow.setLocation(400, 300);
		receivedPicturesWindow.setVisible(false);
		descriptionTextArea.setEditable(false);

	}

	@Override
	public void run() {
		String chat = null;
		
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
					chat = somethingFromTheServer.toString();
					outChatArea.append("\n" + chat);
					outChatArea.setCaretPosition(outChatArea.getDocument().getLength());
					inChatArea.requestFocus();
				} else if(somethingFromTheServer instanceof ImageIcon) {
					ImageIcon newPicture = (ImageIcon)somethingFromTheServer;
					receivedPictures.addElement(newPicture);
					newPicture.setDescription("From " + chatName + chat);
					receivedPicturesList.setListData(receivedPictures);
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
		ImageIcon picture = myPicturesList.getSelectedValue();
		String chatToSend = inChatArea.getText().trim();

// sendToAllButton
		if (ae.getSource() == sendToAllButton) {

			if ((picture != null) || (chatToSend.length() != 0)) {
				System.out.println("Sending " + picture);
				System.out.println("Sending " + chatToSend);			
				inChatArea.setText(""); // clear input area on GUI
				try {
					oos.writeObject(picture);
					oos.writeObject(chatToSend);
				} catch (IOException e) {
					return;
				}
				myPicturesList.clearSelection();
				myPictureWindowLabel.setText("Select a picture.");
			} else {
				chatLabel.setForeground(Color.red);
				chatLabel.setText("NO MESSAGE TO SEND!");
				return;
			}
		}

// sendPrivateButton
		if (ae.getSource() == sendPrivateButton) {
			String chatMessage = inChatArea.getText().trim();
			if (chatMessage.length() == 0) {
				chatLabel.setForeground(Color.red);
				chatLabel.setText("NO MESSAGE TO SEND!");
				return;
			}
			List<String> recipients = whosInList.getSelectedValuesList();
			if (recipients.isEmpty()) {
				chatLabel.setForeground(Color.red);
				chatLabel.setText("NO RECIPIENTS ARE SELECTED");
				return;
			}
			inChatArea.setText(""); // clear
			System.out.println("Sending " + chatMessage + " to private message recipients: " + recipients);
			String[] privateArray = new String[recipients.size()+1];
			privateArray[0] = chatMessage;
			for (int i = 1; i < privateArray.length; i++) {
				privateArray[i] = recipients.get(i-1);
			}
			try {
				oos.writeObject(privateArray);
			} catch(IOException ioe) {} // let run() handle 
			return;
		}

// saveMessageButton
		if (ae.getSource() == saveMessageButton) {
			String chatMessage = inChatArea.getText().trim();
			if (chatMessage.length() == 0) {
				chatLabel.setForeground(Color.red); 
				chatLabel.setText("NO MESSAGE TO SAVE!"); 
				return; 
			}
			List<String> recipients = whosNotInList.getSelectedValuesList();
			if (recipients.isEmpty()) {
				chatLabel.setForeground(Color.red); 
				chatLabel.setText("NO RECIPIENTS ARE SELECTED"); 
				return; 
			}
			inChatArea.setText(""); // clear
			System.out.println("Saving message: " + chatMessage + " for recipients: " + recipients);
			String[] saveArray = new String[recipients.size()+1];
			saveArray[0] = chatMessage;
			for (int i = 1; i < saveArray.length; i++) {
				saveArray[i] = recipients.get(i-1);
			}
			try {
				oos.writeObject(saveArray);
			} catch(IOException ioe) {} // let run() handle 
			return; 
		}

// clearWhosInButton
		if (ae.getSource() == clearWhosInButton) {
			whosInList.clearSelection();
			return;
		}
		
// clearWhosNotInButton
		if (ae.getSource() == clearWhosNotInButton) {
			whosNotInList.clearSelection();
			return;
		}

// previewPicturesButton
		if (ae.getSource() == previewPicturesButton) {
			System.out.println("Local directory is: " + localDirectory);
			picturePreviewWindow.setVisible(true);
			myPictureWindowLabel.setText("");
			String[] listOfFiles = localDirectory.list();

			for (String fileName : listOfFiles) {
				if(fileName.endsWith(".gif") || fileName.endsWith(".jpg") ||fileName.endsWith(".png")) {
					pictureFiles.add(fileName);
					ImagesV.addElement(new ImageIcon(fileName));
				}
				myPicturesList.setListData(ImagesV);
			}
			if (pictureFiles.isEmpty()) {
				System.out.println("No pictures are found in the local directory.");
			} else {
				System.out.println("Local directory pictures are: " + pictureFiles);
			}
		}

//// selectPicturesButton
//		if (ae.getSource() == selectPicturesButton) {
//			System.out.println("selectPicturesButton was pressed");
//			pictureSelectWindow.setVisible(true);
////			myPictureWindowLabel.setText(pictureDescription);
//		}
		
// clearPicSelectButton
		if (ae.getSource() == clearPicSelectButton) {
			myPicturesList.clearSelection();
			myPictureWindowLabel.setText("Select a picture.");
		}

// horizontalSplit
		if (ae.getSource() == horizontalSplit) {
			chatPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			return;
		}

// verticalSplit
		if (ae.getSource() == verticalSplit) {
			chatPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			return;
		}

// showReceivedPicturesButton
		if (ae.getSource() == showReceivedPicturesButton) {
			receivedPicturesWindow.setVisible(true);			
			return;
		}
		
// clearReceivedSelectButton
		if (ae.getSource() == clearReceivedSelectButton) {
			receivedPicturesList.clearSelection();
			descriptionTextArea.setText("");
			return;
		}
		
// eraseReceivedSelectButton
		if(ae.getSource() == eraseReceivedSelectButton) {
			receivedPictures.remove(receivedPicturesList.getSelectedIndex());
			receivedPicturesList.setListData(receivedPictures);
			descriptionTextArea.setText("");
			return;	
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		  if (myPicturesList.getValueIsAdjusting()) {
			  ImageIcon picture = myPicturesList.getSelectedValue();
			  if (picture == null) {
				  return; // nothing selected 
			  }
			  String pictureDescription = picture.getDescription();
			  myPictureWindowLabel.setText(pictureDescription);
		  }
		  if (receivedPicturesList.getValueIsAdjusting()) {
			  ImageIcon picture2 = receivedPicturesList.getSelectedValue();
			  String pictureDescription2 = picture2.getDescription();
			  descriptionTextArea.setText(pictureDescription2);
		  }
	}
}
