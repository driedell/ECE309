import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class BadShapesGame implements MouseListener {

	public BadShapesGame() {
		ShapeObject myShapeProgram = new Square();
		JPanel displayPanel;
		Graphics g;
		myShapeProgram.drawShape(100, 100, 50, g, Color.BLACK, Color.WHITE, myShapeProgram, displayPanel);
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new BadShapesGame();

	}

}
