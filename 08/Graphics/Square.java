// David Riedell dariedel@ncsu.edu

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Square implements ShapeObject {
	MouseListener controller;
	Graphics g;
	int x;
	int y;
	JPanel displayPanel;
	int size;
	int biggestSize;
	int longestString;
	int sizeAdjustment = 50;
	int charToPix = 6;
	int showing;
	Color drawColor;
	Color backgroundColor;
	boolean fill = false;
	boolean shapeIsNotInitialized = true;
	String colorName;


	public String toString() {
		System.out.println("In toString() in " + getClass().getName());
		return getClass().getName();	
	}

	public void mouseClicked(MouseEvent me) {
		this.x = me.getX();
		this.y = me.getY();
		drawShape(this.x, this.y, this.size, this.g, this.drawColor, this.backgroundColor, this.controller, this.displayPanel);
		this.displayPanel.removeMouseListener(this);
		this.displayPanel.addMouseListener(this.controller);
	}

	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}


	public void drawShape(int x, int y, int size, Graphics g, Color drawColor,
			Color backgroundColor, MouseListener controller, JPanel displayPanel)
					throws IllegalArgumentException {
		this.x = x;
		this.y = y;
		this.size = size;
		this.g = g;
		this.drawColor = drawColor;
		this.backgroundColor = backgroundColor;
		this.controller = controller;
		this.displayPanel = displayPanel;

		System.out.println("In drawShape() in " + getClass().getName());
		if (g == null) {
			throw new IllegalArgumentException("Graphics parm is null in drawShape");
		}
		g.setColor(drawColor);
		if (size > 0) {
			this.sizeAdjustment = 10;
		}
		if (size > 49) {
			this.sizeAdjustment = 10;
		}
		if (size > 99) {
			this.sizeAdjustment = 20;
		}
		if (size > 149) {
			this.sizeAdjustment = 30;
		}
		if (size > this.biggestSize) {
			this.biggestSize = size;
		}
		if (this.fill) {
			g.fillRect(x, y, size - this.sizeAdjustment, size);
		} else {
			g.drawRect(x, y, size - this.sizeAdjustment, size);
		}
		this.shapeIsNotInitialized = false;

	}

	public void redraw() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In redraw() in " + getClass().getName());
		erase();
		drawShape(this.x, this.y, this.size, this.g, this.drawColor, this.backgroundColor, this.controller, this.displayPanel);
		checkShowing();
	}

	public void lickTheBigPill() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In lickTheBigPill() in " + getClass().getName());
		this.size += 10;
		if (this.size > this.biggestSize) {
			this.biggestSize = this.size;
		}
		redraw();
	}

	public void expandEmpty() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In expandEmpty() in " + getClass().getName());
		if (this.fill) {
			return;
		}
		this.size += 10;
		if (this.size > this.biggestSize) {
			this.biggestSize = this.size;
		}
		drawShape(this.x, this.y, this.size, this.g, this.drawColor, this.backgroundColor, this.controller, this.displayPanel);
	}

	public void lickTheSmallPill() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In lickTheSmallPill() in " + getClass().getName());
		if (this.size < 10) {
			return;
		}
		this.size -= 10;
		redraw();
	}

	public void shrinkEmpty() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In shrinkEmpty() in " + getClass().getName());
		if (this.fill) {
			return;
		}
		if (this.size < 11) {
			return;
		}
		this.size -= 10;
		drawShape(this.x, this.y, this.size, this.g, this.drawColor, this.backgroundColor, this.controller, this.displayPanel);
	}

	public void rotateLeft90() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In rotateLeft90() in " + getClass().getName());
	}

	public void rotateRight90() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In rotateRight90() in " + getClass().getName());
	}

	public void fill() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In fill() in " + getClass().getName());
		this.fill = true;
		redraw();
	}

	public void empty() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In empty() in " + getClass().getName());
		this.fill = false;
		redraw();
	}

	public void flipUp() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In flipUp() in " + getClass().getName());
	}

	public void flipDown() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In flipDown() in " + getClass().getName());
	}

	public void flipLeft() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In flipLeft() in " + getClass().getName());
	}

	public void flipRight() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In flipRight() in " + getClass().getName());
	}

	public void erase() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In erase() in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 10);
		this.g.fillRect(this.x, this.y, this.biggestSize, this.biggestSize);
		this.g.setColor(this.drawColor);
	}


	public void moveToNextClick() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In moveToNextClick() in " + getClass().getName());
		erase();
		this.displayPanel.removeMouseListener(this.controller);
		this.displayPanel.addMouseListener(this);
	}


	public void copyAtNextClick() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In dupeAtNextClick() in " + getClass().getName());
		this.displayPanel.removeMouseListener(this.controller);
		this.displayPanel.addMouseListener(this);
	}


	public void showArea() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In showArea() in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 11);
		this.g.setColor(Color.black);
		String showString = "Area = length x width = " + this.size*this.size;
		if (showString.length() * this.charToPix > this.longestString) {
			this.longestString = (showString.length() * this.charToPix);
		}
		this.g.drawString(showString, this.x, this.y);
		this.g.setColor(this.drawColor);
		this.showing = 2;
	}


	public void showPerimeter() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In showPerimeter() in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 11);
		this.g.setColor(Color.black);
		String showString = "Area = length x width = " + this.size*this.size;
		if (showString.length() * this.charToPix > this.longestString) {
			this.longestString = (showString.length() * this.charToPix);
		}
		this.g.drawString(showString, this.x, this.y);
		this.g.setColor(this.drawColor);
		this.showing = 3;
	}


	public void showLocation() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In showLocation in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 11);
		this.g.setColor(Color.black);
		String showString = "Location = x,y = " + this.x + "," + this.y;
		if (showString.length() * this.charToPix > this.longestString) {
			this.longestString = (showString.length() * this.charToPix);
		}
		this.g.drawString(showString, this.x, this.y);
		this.g.setColor(this.drawColor);
		this.showing = 5;
	}


	public void showColor() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In showDrawColor() in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 11);
		this.g.setColor(Color.black);
		String showString = "Current draw color is " + this.colorName;
		if (showString.length() * this.charToPix + 10 > this.longestString) {
			this.longestString = (showString.length() * this.charToPix + 10);
		}
		this.g.drawString(showString, this.x, this.y);
		this.g.setColor(this.drawColor);
		this.g.fillRect(this.x + showString.length() * this.charToPix, this.y - 10, 10, 10);
		this.showing = 4;
	}


	public void showSize() {
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In showSize() in " + getClass().getName());
		this.g.setColor(this.backgroundColor);
		System.out.println("longestString = " + this.longestString);
		this.g.fillRect(this.x, this.y - 10, this.longestString, 11);
		this.g.setColor(Color.black);
		String showString = "Size = side length = " + this.size;
		if (showString.length() * this.charToPix > this.longestString) {
			this.longestString = (showString.length() * this.charToPix);
		}
		this.g.drawString(showString, this.x, this.y);
		this.g.setColor(this.drawColor);
		this.showing = 1;
	}


	public void setColor(Color drawColor, String colorName) {
		System.out.println("In setColor() in " + getClass().getName());
		this.drawColor = drawColor;
		this.colorName = colorName;
	}

	private void checkShowing()	{
		if (this.shapeIsNotInitialized) {
			return;
		}
		System.out.println("In checkShowing() in " + getClass().getName());
		switch (this.showing) {
		case 0: 

		case 1: 
			showSize(); break;
		case 2: 
			showArea(); break;
		case 3: 
			showPerimeter(); break;
		case 4: 
			showColor(); break;
		case 5: 
			showLocation();
		}
	}


}
