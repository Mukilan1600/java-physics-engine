import javax.swing.*;
import java.awt.*;

public class GUIFrame extends JFrame {
    public GUIFrame(FrameCanvas canvas, Dimension dimension){
        add(canvas);
        setSize(dimension);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(canvas);
        setVisible(true);

        Dimension contentSize = getContentPane().getSize();
        canvas.setScreenSize(contentSize);
    }
}
