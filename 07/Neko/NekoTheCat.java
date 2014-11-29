import java.applet.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class NekoTheCat implements MouseListener, Runnable {

	public static void main(String[] args) {
		new NekoTheCat();
	}

	Image catRight1 = new ImageIcon(getClass().getResource("Neko1.gif")).getImage();
	Image catRight2 = new ImageIcon(getClass().getResource("Neko2.gif")).getImage();
	Image catLeft1  = new ImageIcon(getClass().getResource("Neko3.gif")).getImage();
	Image catLeft2  = new ImageIcon(getClass().getResource("Neko4.gif")).getImage();
	Image redBall   = new ImageIcon(getClass().getResource("red-ball.gif")).getImage();

	Image cat1 = catRight1;
	Image cat2 = catRight2;
	Image currentImage = catRight1;

	JFrame gameWindow = new JFrame("Neko The Cat!");
	JPanel gamePanel  = new JPanel();

	int     catxPosition  = 1;
	int     catyPosition  = 50;
	int     catWidth      = catRight1.getWidth(gamePanel);
	int     catHeight     = catRight1.getHeight(gamePanel);
	int     ballxPosition = 0;
	int     ballyPosition = 0;
	int     ballSize      = redBall.getWidth(gamePanel); 
	int     sleepTime     = 100; // pause time between image repaints (in ms)
	int     xBump         = 10;  // amount cat image is moved each repaint.
	boolean catIsRunningToTheRight = true; // initially
	boolean catIsRunningToTheLeft  = false;// initially
	boolean ballHasBeenPlaced      = false;// initially

	Graphics g;

	AudioClip soundFile = Applet.newAudioClip(getClass().getResource("spacemusic.au"));


	public NekoTheCat() {
		// Build GUI

		gameWindow.getContentPane().add(gamePanel, "Center");
		gamePanel.setBackground(Color.WHITE);
		gameWindow.setSize(600, 400);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		g = gamePanel.getGraphics();			

		// show game instructions on the screen
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times Roman", Font.BOLD, 20));
		g.drawString("Neko the cat is looking for it's red ball!"         ,10,120);
		g.drawString("Click the mouse to place Neko's ball."              ,10,140);
		g.drawString("Can you move the ball to keep Neko from getting it?",10,160);
		g.drawString("(Pull window larger to make the game easier)"       ,10,180);
		g.setColor(Color.WHITE);
		
		gamePanel.addMouseListener(this);
		new Thread(this).start();		// idk about this line
		soundFile.loop();
	}

	@Override
	public void mouseClicked(MouseEvent me) {	
		g.setColor(Color.white); // set to background color
		g.fillRect(ballxPosition, ballyPosition, ballSize, ballSize);//x,y,width,height

		ballxPosition = me.getX();
		ballyPosition = me.getY();

		System.out.println("Mouse clicked at x=" + ballxPosition + ", y=" + ballyPosition);
		g.drawImage(redBall, ballxPosition, ballyPosition, gamePanel);
		
		ballHasBeenPlaced = true;

	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public void run() {
		System.out.println("app thread has entered run");

		while(true) {
			g = gamePanel.getGraphics();

			while ((catxPosition > 0) && (catxPosition < gamePanel.getSize().width)) {
				// 1. Blank out the last image
				g.setColor(Color.WHITE);
				g.fillRect(catxPosition-10, catyPosition-10, catWidth+10, catHeight+20);
				
				// 2A.Bump the location for the new image
				catxPosition = catxPosition + xBump;

				// 2B.Select the next image.
				if (currentImage == cat1) {
					currentImage = cat2;
				} else {
					currentImage = cat1;
				}

				// 3. Draw the next cat image
				g.drawImage(currentImage, catxPosition, catyPosition, gamePanel);

				// 4. Pause briefly to let human eye see the new image!
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ie){}

				// 5. If necessary, redirect the cat toward the ball.
				if (ballHasBeenPlaced) { // first ensure that the ball is "in play"

					// If cat is BELOW the ball then move cat up 1 line.
					if(catyPosition > ballyPosition) {
						catyPosition = catyPosition - 10;
					}

					// If cat is ABOVE the ball then move cat down one line.
					if(catyPosition < ballyPosition) {
						catyPosition = catyPosition + 10;
					}

					// If the cat is running to the left and the ball is to the right of the cat
					if(catxPosition < ballxPosition && catIsRunningToTheLeft) {
						reverseDirectionFromLeftToRight();
					}

					// If the cat is running to the right and the ball is to the left of the cat 
					if(catxPosition > ballxPosition && catIsRunningToTheRight) {
						reverseDirectionFromRightToLeft();
					}
				}

				// 6. Proximity test to see if Neko is "at" the ball. 
				if ((Math.abs(catyPosition - ballyPosition) < 10)   // y within 10   
						&& (Math.abs(catxPosition - ballxPosition) < 10))  // x within 10 pixels
				{
					// Take Neko-got-the-ball action!
					gamePanel.removeMouseListener(this);
					g.setColor(Color.RED);
					g.setFont(new Font("Times Roman", Font.BOLD, 50));
					g.drawString("At last, I have my ball!", 10, 50);
					soundFile.stop();
					return;
				}


			}

			// turn Neko around.
			if (catxPosition > gamePanel.getSize().width) {
				reverseDirectionFromRightToLeft();
				catxPosition = gamePanel.getSize().width - 1;
			}

			if (catxPosition < 0)
			{
				reverseDirectionFromLeftToRight();
				catxPosition = 1;
			}
		} // bottom of outer while(true) loop
	} // end of run() method


	private void reverseDirectionFromRightToLeft() {
		xBump = -xBump; // reverse increment
		cat1 = catLeft1;
		cat2 = catLeft2;
		catIsRunningToTheLeft  = true;
		catIsRunningToTheRight = false;
	}

	private void reverseDirectionFromLeftToRight() {
		xBump = -xBump;	
		cat1 = catRight1;
		cat2 = catRight2;
		catIsRunningToTheRight = true;
		catIsRunningToTheLeft  = false;
	}


}