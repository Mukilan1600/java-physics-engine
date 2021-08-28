package objects;

import java.awt.*;

public class CircleBody extends RigidBody{
    final double radius;

    public CircleBody(double mass, Vector2 position, Vector2 velocity, double radius, boolean isStatic) {
        super(mass, position, velocity,Type.CIRCLE, isStatic);
        this.radius = radius;
        this.inertia = (Math.PI * Math.pow(radius,4))/4f;

        if(isStatic)
            this.inertiaInverse = 0f;
        else
            this.inertiaInverse = 1f/this.inertia;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)(position.x-radius),(int)(position.y-radius),(int)(radius*2f),(int)(radius*2f));
        g.drawOval((int)(position.x-radius),(int)(position.y-radius),(int)(radius*2f),(int)(radius*2f));
    }

    public void checkAndResolveCollision(RigidBody b, Graphics g) {
        if(b.type==Type.CIRCLE){
            CollisionUtils.checkResolve(this,(CircleBody)b);
        }else if(b.type==Type.POLYGON){
            CollisionUtils.checkResolve(this,(RectangleBody)b, g);
        }
    }
}
