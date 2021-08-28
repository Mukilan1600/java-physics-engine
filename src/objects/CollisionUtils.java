package objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollisionUtils {
    public static void resolveCollision(RigidBody a, RigidBody b, Vector2 normal,double displacement){
        double vAB = Vector2Utils.dotProduct(Vector2Utils.subtract(b.velocity,a.velocity),normal);
        if(a.isStatic)
            b.move(Vector2Utils.multiply(normal,displacement));
        else if(b.isStatic)
            a.move(Vector2Utils.multiply(normal,-displacement));
        else{
            a.move(Vector2Utils.multiply(normal,-displacement/2f));
            b.move(Vector2Utils.multiply(normal,displacement/2f));
        }
            float e = 0.8f;
            double j = (vAB * -(1+e)) / (a.massInverse + b.massInverse);

            Vector2 dVa = Vector2Utils.multiply(normal,-j * a.massInverse);
            Vector2 dVb = Vector2Utils.multiply(normal,j * b.massInverse);

            a.accelerate(dVa);
            b.accelerate(dVb);
    }

    public static void resolveCollision(RigidBody a, RigidBody b, Vector2 normal, double displacement, Vector2 collisionP, Graphics g){
        Vector2 raT = Vector2Utils.subtract(collisionP, a.position),
                rbT = Vector2Utils.subtract(collisionP, b.position);
        Vector2 ra = new Vector2(-raT.y, raT.x),
                rb = new Vector2(-rbT.y, rbT.x);
        double vAB = Vector2Utils.dotProduct(Vector2Utils.subtract(Vector2Utils.add(b.velocity, Vector2Utils.multiply(rb, b.angularVelocity)),Vector2Utils.add(a.velocity, Vector2Utils.multiply(ra, a.angularVelocity))),normal);
        float e = 0.8f;

        g.setColor(Color.CYAN);
        g.fillOval((int)collisionP.x,(int)collisionP.y,5,5);
        g.setColor(Color.WHITE);

        double ta = Math.pow(Vector2Utils.dotProduct(ra, normal),2) * a.inertiaInverse,
                tb = Math.pow(Vector2Utils.dotProduct(rb, normal),2) * b.inertiaInverse;
        double j = (vAB * -(1+e)) / (a.massInverse + b.massInverse + ta + tb);
        Vector2 jNA = Vector2Utils.multiply(normal, -j),jNB = Vector2Utils.multiply(normal, j);

        Vector2 dVa = Vector2Utils.multiply(normal,-j * a.massInverse),
                dVb = Vector2Utils.multiply(normal,j * b.massInverse);
        double dAa = a.inertiaInverse * Vector2Utils.dotProduct(ra, jNA),
                dAb = b.inertiaInverse * Vector2Utils.dotProduct(rb, jNB);

        if(a.isStatic)
            b.move(Vector2Utils.multiply(normal,displacement));
        else if(b.isStatic)
            a.move(Vector2Utils.multiply(normal,-displacement));
        else{
            a.move(Vector2Utils.multiply(normal,-displacement/2f));
            b.move(Vector2Utils.multiply(normal,displacement/2f));
        }

        a.accelerate(dVa);
        a.accelerateAngular(dAa);
        b.accelerate(dVb);
        b.accelerateAngular(dAb);
    }

    public static void checkResolve(CircleBody a, CircleBody b){
        double distanceBetweenC = Vector2Utils.distance(a.position,b.position);
        double radiiSum = a.radius+b.radius;
        if(distanceBetweenC<radiiSum){
            Vector2 normal = Vector2Utils.normalize(Vector2Utils.subtract(b.position,a.position));
            CollisionUtils.resolveCollision(a,b,normal,radiiSum-distanceBetweenC);
        }
    }

    public static void checkResolve(RectangleBody a, RectangleBody b, Graphics g){
        Vector2 minimumNormal = Vector2.VECTOR2_ZERO();
        double displacement = Double.MAX_VALUE;
        boolean collide = true;
        Vector2 collisionPoint = Vector2.VECTOR2_ZERO();
        Vector2 centerV = Vector2Utils.subtract(b.position, a.position);
        List<List<Vector2>> ext = new ArrayList<>();
        String who="";
        for(int i=0; i<4;i++){
            Vector2 v1 = Vector2Utils.add(a.vertices[i],a.position);
            Vector2 v2 = Vector2Utils.add(a.vertices[(i+1)%4],a.position);
            Vector2 edge = Vector2Utils.subtract(v2,v1);
            Vector2 normal = new Vector2(-edge.y,edge.x);
            double magA = Vector2Utils.magnitude(edge);

            List<List<Vector2>> extremesA = getExtremeVertices(a.vertices, a.position, normal),
                    extremesB = getExtremeVertices(b.vertices, b.position, normal);

            double minA = Vector2Utils.dotProduct(extremesA.get(0).get(0),normal),
                    maxA = Vector2Utils.dotProduct(extremesA.get(1).get(0),normal),
                    minB = Vector2Utils.dotProduct(extremesB.get(0).get(0),normal),
                    maxB = Vector2Utils.dotProduct(extremesB.get(1).get(0),normal);

            if(maxA <= minB || maxB <= minA)
                collide = false;
            else if(collide) {
                double displacementCorrection = Math.min(maxA - minB, maxB - minA);
                if (displacementCorrection < displacement) {
                    displacement = displacementCorrection;
                    minimumNormal = normal;
                    collisionPoint = getCollisionPoint(centerV, minimumNormal, extremesB, extremesA, magA, true);
                    ext = extremesB;
                    who = "A";
                }
            }

        }

        for(int i=0; i<4;i++){
            Vector2 v1 = Vector2Utils.add(b.vertices[i],b.position);
            Vector2 v2 = Vector2Utils.add(b.vertices[(i+1)%4],b.position);
            Vector2 edge = Vector2Utils.subtract(v2,v1);
            Vector2 normal = new Vector2(-edge.y,edge.x);
            double magB = Vector2Utils.magnitude(edge);

            List<List<Vector2>> extremesA = getExtremeVertices(a.vertices, a.position, normal),
                    extremesB = getExtremeVertices(b.vertices, b.position, normal);

            double minA = Vector2Utils.dotProduct(extremesA.get(0).get(0),normal),
                    maxA = Vector2Utils.dotProduct(extremesA.get(1).get(0),normal),
                    minB = Vector2Utils.dotProduct(extremesB.get(0).get(0),normal),
                    maxB = Vector2Utils.dotProduct(extremesB.get(1).get(0),normal);

            if(maxA <= minB || maxB <= minA)
                collide = false;
            else if(collide) {
                double displacementCorrection = Math.min(maxA - minB, maxB - minA);
                if (displacementCorrection < displacement) {
                    displacement = displacementCorrection;
                    minimumNormal = normal;
                    collisionPoint = getCollisionPoint(centerV, minimumNormal, extremesA, extremesB, magB, false);
                    who = "B";
                }
            }
        }

        if(Vector2Utils.dotProduct(centerV,minimumNormal)<0) {
            minimumNormal = Vector2Utils.multiply(minimumNormal, -1);
        }

        if(collide) {
            if(who.equals("A")) {
                System.out.println(who);
                System.out.println(collisionPoint);
                System.out.println(ext);
                System.out.println(Arrays.toString(b.vertices));
                System.out.println(b.position);
                getExtremeVertices(b.vertices, b.position, minimumNormal, true);
            }
            resolveCollision(a, b, Vector2Utils.normalize(minimumNormal), displacement/Vector2Utils.magnitude(minimumNormal), collisionPoint, g);
        }
    }

    public static void checkResolve(CircleBody a, RectangleBody b, Graphics g){
        Vector2 minimumNormal = Vector2.VECTOR2_ZERO(), collisionPoint = Vector2.VECTOR2_ZERO();
        double displacement = Double.MAX_VALUE;
        boolean collide = true;

        Vector2 closestVertex = new Vector2(Double.MAX_VALUE,Double.MAX_VALUE);

        for(int i=0; i<4;i++){
            Vector2 v1 = Vector2Utils.add(b.vertices[i],b.position);
            Vector2 v2 = Vector2Utils.add(b.vertices[(i+1)%4],b.position);
            Vector2 edge = Vector2Utils.subtract(v2,v1);
            Vector2 normal = new Vector2(-edge.y,edge.x);

            double[] projA = projectCircleOntoNormal(a,normal);
            double[] projB = projectVerticesOntoNormal(b.vertices, normal, b.position);
            double minA = projA[0], maxA = projA[1], minB = projB[0], maxB = projB[1];

            if(maxA <= minB || maxB <= minA)
                collide = false;
            else {
                double displacementCorrection = Math.min(maxA - minB, maxB - minA);
                if (displacementCorrection < displacement) {
                    displacement = displacementCorrection;
                    minimumNormal = normal;
                }
            }

            if(Vector2Utils.distance(a.position, b.vertices[i])<Vector2Utils.distance(a.position, closestVertex))
                closestVertex = b.vertices[i];
        }

        Vector2 normal = Vector2Utils.subtract(closestVertex,a.position);

        double[] projA = projectCircleOntoNormal(a,normal);;
        double[] projB = projectVerticesOntoNormal(b.vertices, normal, b.position);
        double minA = projA[0], maxA = projA[1], minB = projB[0], maxB = projB[1];

        double displacementCorrection = Math.min(maxA - minB, maxB - minA);

        if(maxA <= minB || maxB <= minA)
            collide = false;
        else if (displacementCorrection < displacement) {
                displacement = displacementCorrection;
                minimumNormal = normal;
                collisionPoint = closestVertex;
        }


        Vector2 centerV = Vector2Utils.subtract(b.position, a.position);
        if(Vector2Utils.dotProduct(centerV,minimumNormal)<0)
            minimumNormal = Vector2Utils.multiply(minimumNormal,-1);
        if(displacementCorrection >= displacement) {
            Vector2 normalInv = Vector2Utils.normalize(minimumNormal);
            collisionPoint = Vector2Utils.add(Vector2Utils.multiply(normalInv, a.radius), a.position);
        }

        if(collide) {
            resolveCollision(a, b, Vector2Utils.normalize(minimumNormal),displacement/Vector2Utils.magnitude(minimumNormal), collisionPoint, g);
        }
    }

    private static Vector2 getCollisionPoint(Vector2 centerV, Vector2 minimumNormal, List<List<Vector2>> extremesA, List<List<Vector2>> extremesB, double magB, boolean invert){
        int minI=0,maxI=1;
        if(invert){
            minI=1;
            maxI=0;
        }
        if(Vector2Utils.dotProduct(centerV,minimumNormal)<0) {
            if(extremesA.get(minI).size()>1)
            {
                double magA = Vector2Utils.magnitude(Vector2Utils.subtract(extremesA.get(minI).get(0),extremesA.get(minI).get(1)));
                if(magA<magB){
                    return Vector2Utils.divide(Vector2Utils.add(extremesA.get(minI).get(0),extremesA.get(minI).get(1)),2f);
                }else{
                    return Vector2Utils.divide(Vector2Utils.add(extremesB.get(maxI).get(0),extremesB.get(maxI).get(1)),2f);
                }
            }else {
                return extremesA.get(minI).get(0);
            }
        }else{

            if(extremesA.get(maxI).size()>1)
            {
                double magA = Vector2Utils.magnitude(Vector2Utils.subtract(extremesA.get(maxI).get(0),extremesA.get(maxI).get(1)));
                if(magA<magB){
                    return Vector2Utils.divide(Vector2Utils.add(extremesA.get(maxI).get(0),extremesA.get(maxI).get(1)),2f);
                }else{
                    return Vector2Utils.divide(Vector2Utils.add(extremesB.get(minI).get(0),extremesB.get(minI).get(1)),2f);
                }
            }else {
                return extremesA.get(maxI).get(0);
            }
        }
    }

    private static List<List<Vector2>> getExtremeVertices(Vector2[] vertices, Vector2 position, Vector2 normal){
        return getExtremeVertices(vertices, position, normal, false);
    }

    private static List<List<Vector2>> getExtremeVertices(Vector2[] vertices, Vector2 position, Vector2 normal, boolean debug) {
        List<Vector2> minV = new ArrayList<>(), maxV = new ArrayList<>();
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, minDiff = Double.MAX_VALUE;

        for(int k=0;k<4;k++){
            Vector2 vb = Vector2Utils.add(vertices[k],position);
            double mB = Vector2Utils.dotProduct(vb, normal);
            if(debug){
                System.out.println(vb);
                System.out.println(minV);
                System.out.println(Math.abs(mB - min));
            }
            if(Math.abs(mB - min)<0.01f){
                minV.add(vb);
            }else if(mB<min){
                min = mB;
                minV = new ArrayList<>(Collections.singletonList(vb));
            }
            if(Math.abs(mB-max)<minDiff) {
                minDiff = Math.min(Math.abs(mB - max), minDiff);
            }

            if(Math.abs(mB - max)<0.01f){
                maxV.add(vb);
            } else if(mB>max){
                max = mB;
                maxV = new ArrayList<>(Collections.singletonList(vb));
            }

        }
        return new ArrayList<>(Arrays.asList(minV,maxV));
    }

    private static double[] projectCircleOntoNormal(CircleBody a, Vector2 normal){
        Vector2 radiusV = Vector2Utils.multiply(Vector2Utils.normalize(normal),a.radius);
        Vector2 l = Vector2Utils.subtract(a.position,radiusV), r = Vector2Utils.add(a.position, radiusV);
        return new double[]{Vector2Utils.dotProduct(l,normal), Vector2Utils.dotProduct(r,normal)};
    }

    private static double[] projectVerticesOntoNormal(Vector2[] vertices, Vector2 normal, Vector2 position){
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for(int k=0;k<4;k++){
            Vector2 vb = Vector2Utils.add(vertices[k],position);
            double mB = Vector2Utils.dotProduct(vb, normal);
            min = Math.min(mB,min);
            max = Math.max(mB,max);
        }

        return new double[]{min, max};
    }
}
