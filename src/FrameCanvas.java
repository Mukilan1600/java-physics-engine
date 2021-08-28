import objects.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FrameCanvas extends JPanel implements ActionListener, KeyListener {
    List<RigidBody> objects;
    double frameTime;
    boolean[] keysPressed = new boolean[4];
    Dimension screenSize;

    public FrameCanvas(Dimension dimension) {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        new Timer(4,this).start();

        frameTime = System.currentTimeMillis();
        setSize(dimension);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        double frameInterval = (System.currentTimeMillis() - frameTime)/1000f;
        frameTime = System.currentTimeMillis();
        for(int i=0;i<objects.size();i++){
            objects.get(i).move(frameInterval, Vector2.VECTOR2_ZERO());
            objects.get(i).draw(g);
            for(int j=i+1;j<objects.size();j++){
                objects.get(i).checkAndResolveCollision(objects.get(j), g);
            }
        }
        Vector2 translation = Vector2.VECTOR2_ZERO();

        if(keysPressed[0])
            translation.x+=1f;
        if(keysPressed[1])
            translation.y-=1f;
        if(keysPressed[2])
            translation.x-=1f;
        if(keysPressed[3])
            translation.y+=1f;

        if(translation.x!=0 || translation.y!=0)
            objects.get(0).accelerate(Vector2Utils.multiply(Vector2Utils.normalize(translation), 15f * PhysicsEngine.METRIC_SCALE_FACTOR * frameInterval));

    }

    public void actionPerformed(ActionEvent e) {
        System.out.printf("Frames: %.1f \n", 1000f/(System.currentTimeMillis() - frameTime));
        repaint();
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
        setSize(screenSize);
        objects = new ArrayList<>();
        Vector2 position;
        double radius, mass;

        for(int i=0;i<10;i++){
            position = new Vector2(15 + Math.random()*(PhysicsEngine.WINDOW_DIMENSION.width-30),15 + Math.random()*((PhysicsEngine.WINDOW_DIMENSION.height-55)));
            radius = 15f + Math.random()*15f;
            mass = 500f + Math.random()*70000f;

            if(i%2==0) {
                objects.add(new RectangleBody(1000f, position, new Vector2(0, 0), radius * 2, radius * 2, false));
            }else
                objects.add(new CircleBody(1000f,position,new Vector2(0,0),radius,false));
        }

        double w=screenSize.width,h=screenSize.height;
        objects.add(new RectangleBody(1000f, new Vector2(-5, h/2f),new Vector2(0,0), 50, h,true));
        objects.add(new RectangleBody(1000f, new Vector2(w+5, h/2f),new Vector2(0,0), 50, h,true));
        objects.add(new RectangleBody(1000f, new Vector2(w/2f, h),new Vector2(0,0), w-50, 50,true));
        objects.add(new RectangleBody(1000f, new Vector2(w/2f, 0),new Vector2(0,0), w-50, 50,true));
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            keysPressed[0] = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            keysPressed[1] = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            keysPressed[2] = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            keysPressed[3] = true;
        }

    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            keysPressed[0] = false;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            keysPressed[1] = false;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            keysPressed[2] = false;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            keysPressed[3] = false;
        }
    }

    public void keyTyped(KeyEvent e) {

    }
}
