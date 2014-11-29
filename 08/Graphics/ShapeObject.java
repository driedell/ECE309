import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

public interface ShapeObject extends MouseListener
{
void   drawShape (int              x,
                  int              y,
                  int              size,
                  Graphics         g,
                  Color            drawColor, 
                  Color            backgroundColor,
                  MouseListener    controller, 
                  JPanel           displayPanel)
                  throws           IllegalArgumentException; 
void   redraw();
void   lickTheBigPill();
void   expandEmpty();
void   lickTheSmallPill();
void   shrinkEmpty();
void   rotateLeft90();
void   rotateRight90();
void   fill();
void   empty();
void   flipUp();
void   flipDown();
void   flipLeft();
void   flipRight();
void   erase();
void   moveToNextClick();
void   copyAtNextClick();
void   showArea();
void   showPerimeter();
void   showLocation();
void   showColor();
void   showSize();
void   setColor(Color drawColor, String colorName);
String toString();

public static final int NOT_SHOWING       = 0;
public static final int SHOWING_SIZE      = 1;
public static final int SHOWING_AREA      = 2;
public static final int SHOWING_PERIMETER = 3;
public static final int SHOWING_COLOR     = 4;
public static final int SHOWING_LOCATION  = 5;




}
