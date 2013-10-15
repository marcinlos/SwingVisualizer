package mlos.sgl.core;

import com.google.common.base.Predicate;

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
    
    public static double lerp(double t, double a, double b) {
        return t * b + (1 - t) * a;
    }
    
    public static Vec2d lerp(double t, Vec2d a, Vec2d b) {
        double x = lerp(t, a.x, b.x);
        double y = lerp(t, a.y, b.y);
        return new Vec2d(x, y);
    }
    
    public static Vec2d move(Vec2d v, Vec2d d) {
        return sum(v, d);
    }
    
    public static Segment move(Segment s, Vec2d d) {
        return new Segment(move(s.a, d), move(s.b, d));
    }
    
    public static Vec2d lerp(double t, Segment s) {
        return lerp(t, s.a, s.b);
    }
    
    public static Vec2d toVec(Segment s) {
        return diff(s.b, s.a);
    }

    public static Rect aabb(Vec2d... points) {
        double left = Double.POSITIVE_INFINITY;
        double right = Double.NEGATIVE_INFINITY;
        double top = Double.NEGATIVE_INFINITY;
        double bottom = Double.POSITIVE_INFINITY;
        
        for (Vec2d p : points) {
            if (p.x < left) {
                left = p.x;
            }
            if (p.x > right) {
                right = p.x;
            }
            if (p.y < bottom) {
                bottom = p.y;
            }
            if (p.y > top) {
                top = p.y;
            }
        }
        return Rect.bounds(left, bottom, right, top);
    }
    
    
    public static boolean inInterval(double t, double a, double b) {
        return a <= t && t <= b;
    }
    
    public static boolean inRect(Rect rect, Vec2d p) {
        return inInterval(p.x, rect.left(), rect.right())
                && inInterval(p.y, rect.bottom(), rect.top());
    }
    
    public static boolean inCircle(double r, Vec2d p) {
        return normSq(p) <= r * r;
    }
    
    public static boolean inSquare(double r, Vec2d p) {
        return maxNorm(p) <= r;
    }
    
    
    
    public static Predicate<Vec2d> inRectPred(final Rect rect) {
        return new Predicate<Vec2d>() {

            @Override
            public boolean apply(Vec2d p) {
                return inRect(rect, p);
            }
        };
    }
    
    public static Predicate<Vec2d> inCirclePred(final double r) {
        return new Predicate<Vec2d>() {

            @Override
            public boolean apply(Vec2d p) {
                return inCircle(r, p);
            }
        };
    }
    
    public static Predicate<Vec2d> inSquarePred(final double r) {
        return new Predicate<Vec2d>() {

            @Override
            public boolean apply(Vec2d p) {
                return inSquare(r, p);
            }
        };
    }
    
}
