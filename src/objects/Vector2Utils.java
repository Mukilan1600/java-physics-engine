package objects;

public class Vector2Utils {
    public static Vector2 add(Vector2 a, Vector2 b){
        return new Vector2(a.x+b.x, a.y+b.y);
    }

    public static Vector2 subtract(Vector2 a, Vector2 b){
        return new Vector2(a.x-b.x, a.y-b.y);
    }

    public static Vector2 multiply(Vector2 a, double b){
        return new Vector2(a.x*b, a.y*b);
    }

    public static Vector2 divide(Vector2 a, double b){
        return new Vector2(a.x/b, a.y/b);
    }

    public static double magnitude(Vector2 a){
        return Math.sqrt(a.x*a.x + a.y*a.y);
    }

    public static double dotProduct(Vector2 a, Vector2 b){
        return a.x*b.x + a.y*b.y;
    }

    public static double crossProduct(Vector2 a, Vector2 b){
        return a.x*b.y - a.y*b.x;
    }

    public static Vector2 normalize(Vector2 a){
        double magnitude = magnitude(a);
        return new Vector2(a.x/magnitude, a.y/magnitude);
    }

    public static double distance(Vector2 a, Vector2 b){
        return Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
    }

    public static Vector2 round(Vector2 a){ return new Vector2((int)a.x,(int)a.y); }
}
