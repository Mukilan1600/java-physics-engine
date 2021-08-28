package objects;

public abstract class RigidBody implements GraphicObject, Collidable {
    double mass;
    double massInverse;
    double inertia;
    double inertiaInverse;
    Vector2 position;
    Vector2 velocity;
    double angularVelocity;
    Type type;
    boolean isStatic;
    Vector2[] vertices;

    public RigidBody(double mass, Vector2 position, Vector2 velocity, Type type, boolean isStatic) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.type = type;
        this.isStatic = isStatic;

        if(isStatic)
            massInverse = 0f;
        else
            massInverse = 1f/mass;
    }

    public void move(double frameInterval, Vector2 gravity){
        if(!isStatic) {
            Vector2 correctedg = Vector2Utils.multiply(gravity,frameInterval);
            velocity = Vector2Utils.add(velocity,correctedg);
            position =  Vector2Utils.add(position, Vector2Utils.multiply(velocity, frameInterval));
            if (angularVelocity != 0 && type==Type.POLYGON) {
                rotateVertices(angularVelocity * frameInterval);
            }
        }
    }

    void rotateVertices(double angle){
//        x = x*cosa - y*sina
//        y = x*sina + y*cosa
        double cosa = Math.cos(angle);
        double sina = Math.sin(angle);
        for(int i=0;i<vertices.length;i++){
            Vector2 vertex = vertices[i];
            double x = vertex.x*cosa - vertex.y*sina;
            double y = vertex.x*sina + vertex.y*cosa;

            vertices[i] = new Vector2(x,y);
        }
    }

    public void move(Vector2 dPosition){
        position = Vector2Utils.add(position,dPosition);
    }

    public void moveTo(Vector2 newPosition){
        position = newPosition;
    }

    public void accelerate(Vector2 dVelocity){
        velocity = Vector2Utils.add(velocity,dVelocity);
    }

    public void accelerateAngular(double dVelocity){
        if(!isStatic)
            angularVelocity += dVelocity;
    }

}
