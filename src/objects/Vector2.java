package objects;

public class Vector2 {

    public static Vector2 VECTOR2_ZERO(){ return new Vector2(0f,0f); }

    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "{"+this.x+", "+this.y+"}";
    }
}
