package mlos.sgl.core;

public class Geometry {

    private Geometry() {
        // non-instantiable
    }

    public static final Vec2d ZERO = new Vec2d();

    public static Vec2d sum(Vec2d a, Vec2d b) {
        return new Vec2d(a.x + b.x, a.y + b.y);
    }

    public static Vec2d diff(Vec2d a, Vec2d b) {
        return new Vec2d(a.x - b.x, a.y - b.y);
    }

    public static Vec2d neg(Vec2d a) {
        return new Vec2d(-a.x, -a.y);
    }

    public static Vec2d mul(double s, Vec2d a) {
        return new Vec2d(s * a.x, s * a.y);
    }

    public static double lengthSq(Segment s) {
        return distSq(s.a, s.b);
    }
    
    public static double length(Segment s) {
        return dist(s.a, s.b);
    }

    public static double norm(Vec2d a) {
        return Math.hypot(a.x, a.y);
    }
    
    public static double normSq(Vec2d a) {
        return a.x * a.x + a.y * a.y;
    }

    public static double maxNorm(Vec2d a) {
        return Math.max(Math.abs(a.x), Math.abs(a.y));
    }

    public static double distSq(Vec2d a, Vec2d b) {
        return normSq(diff(a, b));
    }

    public static double dist(Vec2d a, Vec2d b) {
        return norm(diff(a, b));
    }

    public static double maxDist(Vec2d a, Vec2d b) {
        return maxNorm(diff(a, b));
    }

    public static Vec2d normalize(Vec2d a) {
        double n = norm(a);
        if (Math.abs(n) > 1e-20) {
            return mul(1 / n, a);
        } else {
            throw new ArithmeticException("Cannot normalize zero-length vector");
        }
    }

    public static double dot(Vec2d a, Vec2d b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vec2d project(Segment axis, Vec2d p) {
        return project(axis.a, axis.b, p);
    }
    
    public static Vec2d project(Vec2d a, Vec2d b, Vec2d p) {
        Vec2d ab = diff(b, a);
        Vec2d ap = diff(p, a);
        double t = dot(ab, ap) / normSq(ab);
        return lerp(t, a, b);
    }
    
    public static double clamp(double t, double a, double b) {
        return t < a ? a : t > b ? b : t;
    }
    
    public static Vec2d lerp(double t, Vec2d a, Vec2d b) {
        double x = t * b.x + (1 - t) * a.x;
        double y = t * b.y + (1 - t) * a.y;
        return new Vec2d(x, y);
    }
    
    public static Vec2d lerp(double t, Segment s) {
        return lerp(t, s.a, s.b);
    }
    
}
