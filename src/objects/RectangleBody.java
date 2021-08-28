package objects;

import java.awt.*;

public class RectangleBody extends RigidBody{

    double width, height;

    public RectangleBody(double mass, Vector2 position, Vector2 velocity, double width, double height, boolean isStatic) {
        super(mass, position, velocity, Type.POLYGON, isStatic);
        this.width = width;
        this.height = height;
        calculateVertices(width, height);
        double b = Math.min(width, height), h = Math.max(width, height);
        this.inertia = (b * Math.pow(h,3))/12;

        if(isStatic)
            this.inertiaInverse = 0f;
        else
            this.inertiaInverse = 1f/this.inertia;
    }

    public RectangleBody(double mass, Vector2 position, Vector2 velocity, double width, double height, boolean isStatic, double initialRotation) {
        super(mass, position, velocity, Type.POLYGON, isStatic);
        this.width = width;
        this.height = height;
        calculateVertices(width, height);
        double b = Math.min(width, height), h = Math.max(width, height);
        this.inertia = (b * Math.pow(h,3))/12;

        if(isStatic)
            this.inertiaInverse = 0f;
        else
            this.inertiaInverse = 1f/this.inertia;

        this.rotateVertices(initialRotation);
    }

    private void calculateVertices(double width, double height){
        double l = -width/2f, r =  l+width, t = -height/2f, b = t+height;
        vertices = new Vector2[]{new Vector2(l, t), new Vector2(r, t), new Vector2(r,b), new Vector2(l,b)};
    }

    public void checkAndResolveCollision(RigidBody b, Graphics g) {
        if(b.type==Type.POLYGON){
            CollisionUtils.checkResolve(this,(RectangleBody) b, g);
        }else if(b.type==Type.CIRCLE){
            CollisionUtils.checkResolve((CircleBody)b,this, g);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        int nP=vertices.length;
        int[] xP = new int[nP], yP = new int[nP];

        for(int i=0;i<nP;i++){
            Vector2 vertex = Vector2Utils.add(vertices[i], position);
            xP[i] = (int)vertex.x;
            yP[i] = (int)vertex.y;
        }
        g.fillPolygon(xP, yP, nP);
        g.drawPolygon(xP,yP,nP);
    }
}
