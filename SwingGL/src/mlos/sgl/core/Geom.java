package mlos.sgl.core;

public class Geom {

    private Geom() {
        // non-instantiable
    }

    public static final Point ZERO = new Point();

    public static Point sum(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point diff(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point neg(Point a) {
        return new Point(-a.x, -a.y);
    }

    public static Point mul(double s, Point a) {
        return new Point(s * a.x, s * a.y);
    }

    public static double lengthSq(Segment s) {
        return distSq(s.a, s.b);
    }
    
    public static double length(Segment s) {
        return dist(s.a, s.b);
    }

    public static double norm(Point a) {
        return Math.hypot(a.x, a.y);
    }
    
    public static double normSq(Point a) {
        return a.x * a.x + a.y * a.y;
    }

    public static double maxNorm(Point a) {
        return Math.max(Math.abs(a.x), Math.abs(a.y));
    }

    public static double distSq(Point a, Point b) {
        return normSq(diff(a, b));
    }

    public static double dist(Point a, Point b) {
        return norm(diff(a, b));
    }

    public static double maxDist(Point a, Point b) {
        return maxNorm(diff(a, b));
    }

    public static Point normalize(Point a) {
        double n = norm(a);
        if (Math.abs(n) > 1e-20) {
            return mul(1 / n, a);
        } else {
            throw new ArithmeticException("Cannot normalize zero-length vector");
        }
    }

    public static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Point project(Segment axis, Point p) {
        return project(axis.a, axis.b, p);
    }
    
    public static Point project(Point a, Point b, Point p) {
        Point ab = diff(b, a);
        Point ap = diff(p, a);
        double t = dot(ab, ap) / normSq(ab);
        return lerp(t, a, b);
    }
    
    public static Point lerp(double t, Point a, Point b) {
        double x = t * b.x + (1 - t) * a.x;
        double y = t * b.y + (1 - t) * a.y;
        return new Point(x, y);
    }

}
