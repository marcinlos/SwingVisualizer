package mlos.sgl.core;

public class Transforms {

    private Transforms() {
        // non-instantiable
    }
    
    public static final Transform ID = new Transform();
    
    
    public static Transform t(double dx, double dy) {
        return new Transform.Builder().t(dx, dy).create();
    }

    public static Transform t(Vec2d v) {
        return new Transform.Builder().t(v).create();
    }
    
    public static Transform tX(double dx) {
        return t(dx, 0);
    }
    
    public static Transform tY(double dy) {
        return t(0, dy);
    }
    
    public static Transform s(double sx, double sy) {
        return new Transform.Builder().s(sx, sy).create();
    }
    
    public static Transform sX(double sx) {
        return s(sx, 1);
    }
    
    public static Transform sY(double sy) {
        return s(1, sy);
    }

    public static Transform r(double theta) {
        return new Transform.Builder().r(theta).create();
    }
    
    public static Transform invert(Transform t) {
        return new Transform.Builder(t).invert().create();
    }
    
    public static Transform compose(Transform first, Transform... ts) {
        Transform.Builder builder = new Transform.Builder(first);
        for (Transform t : ts) {
            builder.apply(t);
        }
        return builder.create();
    }
}
