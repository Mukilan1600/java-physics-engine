package objects;

import java.awt.*;

public interface Collidable {
    enum Type {CIRCLE, POLYGON}
    void checkAndResolveCollision(RigidBody b, Graphics g);
}
