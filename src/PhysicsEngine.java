import objects.Vector2;

import java.awt.*;

public class PhysicsEngine {
    static final Dimension WINDOW_DIMENSION;
    static final Vector2 g;
    static final float METRIC_SCALE_FACTOR = 15f;


    static {
        WINDOW_DIMENSION = new Dimension(1100,600);
        g =  new Vector2(0,9.81f * METRIC_SCALE_FACTOR);
    }

    public static void main(String[] args){
        FrameCanvas canvas = new FrameCanvas(WINDOW_DIMENSION);
        GUIFrame frame = new GUIFrame(canvas, WINDOW_DIMENSION);
    }


}
