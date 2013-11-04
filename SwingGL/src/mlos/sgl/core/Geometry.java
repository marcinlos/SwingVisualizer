package mlos.sgl.core;

import java.util.Comparator;

import com.google.common.base.Predicate;

public class Geometry {
    
    private Geometry() {
        // non-instantiable
    }

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
    
    public static Vec2d normal(Vec2d a, Vec2d b) {
        Vec2d d = diff(b, a);
        return normalize(new Vec2d(d.y, -d.x));
    }
    
    public static Vec2d center(Vec2d... points) {
        double sx = 0;
        double sy = 0;
        for (Vec2d v : points) {
            sx += v.x;
            sy += v.y;
        }
        int n = points.length;
        return n == 0 ? Vec2d.ZERO : new Vec2d(sx / n, sy / n);
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
    
    public static Vec2d lerpPoly(double t, Vec2d... points) {
        if (t == points.length - 1) {
            return points[points.length - 1];
        } else if (t < points.length - 1) {
            int n = (int) t;
            Vec2d a = points[n];
            Vec2d b = points[n + 1];
            double t2 = t - n;
            return lerp(t2, a, b);
        } else {
            throw new IllegalArgumentException("Parameter too large");
        }
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
    
    public static double orient2d(Vec2d a, Vec2d b, Vec2d c) {
        return fastOrient2d_v2(a, b, c);
    }
    
    public static Segment swapEndpoints(Segment s) {
        return new Segment(s.b, s.a);
    }
    
    public static Segment leftToRight(Segment s) {
        return s.a.x <= s.b.x ? s : swapEndpoints(s);
    }
    
    public static double fastOrient2d(Vec2d a, Vec2d b, Vec2d c) {
      return a.x * b.y - a.y * b.x 
              - a.x * c.y + a.y * c.x 
              + b.x * c.y - b.y * c.x;
    }
    
    public static double fastOrient2d_v2(Vec2d a, Vec2d b, Vec2d c) {
        return (a.x - c.x) * (b.y - c.y) - (a.y - c.y)* (b.x - c.x)  ;
      }
    
    public static double exactOrient2d(Vec2d a, Vec2d b, Vec2d c) {
        return Schewchuck.schewchuckOrient2d(a.x, a.y, b.x, b.y, c.x, c.y); 
    }
    
    public static Orientation orientation(Vec2d a, Vec2d b, Vec2d c) {
        double r = orient2d(a, b, c);
        return r == 0 ? Orientation.COLINEAR : 
            r > 0 ? Orientation.CCW : Orientation.CW;
    }
    
    public static boolean colinear(Vec2d a, Vec2d b, Vec2d c) {
        return orient2d(a, b, c) == 0;
    }
    
    public static boolean ccw(Vec2d a, Vec2d b, Vec2d c) {
        return orient2d(a, b, c) > 0;
    }
    
    public static boolean cw(Vec2d a, Vec2d b, Vec2d c) {
        return orient2d(a, b, c) < 0;
    }
    
    public static double incircle(Vec2d a, Vec2d b, Vec2d c, Vec2d d) {
        return Schewchuck.schewchuckIncircle(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y);
    }
    
    public static boolean properIntersect(Segment p, Segment q) {
        Orientation pqa = orientation(p.a, p.b, q.a);
        Orientation pqb = orientation(p.a, p.b, q.b);
        Orientation qpa = orientation(q.a, q.b, p.a);
        Orientation qpb = orientation(q.a, q.b, p.b);
        return pqa != pqb && qpa != qpb;
    }
    
    public static double intersectionParam(Segment p, Segment q) {
        Vec2d pp = toVec(p);
        Vec2d qq = toVec(q);
        double nom = orient2d(p.a, q.a, q.b);
        double denom = pp.x * qq.y - pp.y * qq.x;
        return nom / denom;
    }
    
    public static Vec2d intersectionPoint(Segment p, Segment q) {
        double t = intersectionParam(p, q);
        return lerp(t, p);
    }
    
    public int removeColinear(Vec2d[] v) {
        int j = 1;
        for (int i = 2; i < v.length; ++ i) {
            if (orient2d(v[j - 1], v[j], v[i]) == 0) {
                v[j] = v[i];
            } else {
                v[++ j] = v[i];
            }
        }
        return j;
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
    
    private static double calcParam(double a, double b, double x, double def) {
        return a == b ? def : (x - a) / (b - a);
    }
    
    public static Segment clipLine(Vec2d a, Vec2d b, Rect bounds) {
        double inf = Double.POSITIVE_INFINITY;
        double thmin = calcParam(a.x, b.x, bounds.left(), -inf);
        double thmax = calcParam(a.x, b.x, bounds.right(), inf);
        double tvmin = calcParam(a.y, b.y, bounds.bottom(), -inf);
        double tvmax = calcParam(a.y, b.y, bounds.top(), inf);
        
        double tmin = Math.max(thmin, tvmin);
        double tmax = Math.min(thmax, tvmax);
        return new Segment(lerp(tmin, a, b), lerp(tmax, a, b));
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
    
    public static boolean inTriangle(Vec2d p, Vec2d a, Vec2d b, Vec2d c) {
        return ccw(a, b, p) && ccw(b, c, p) && ccw(c, a, p);
    }
    
    public static final Comparator<Vec2d> LEXICOGRAPHIC = new Comparator<Vec2d>() {
        @Override
        public int compare(Vec2d o1, Vec2d o2) {
            if (o1.x != o2.x) {
                return Double.compare(o1.x, o2.x);
            } else {
                return Double.compare(o1.y, o2.y);
            }
        }
    };
    
    public static final Comparator<Vec2d> YX_LEXICOGRAPHIC = new Comparator<Vec2d>() {
        @Override
        public int compare(Vec2d o1, Vec2d o2) {
            if (o1.y != o2.y) {
                return Double.compare(o1.y, o2.y);
            } else {
                return Double.compare(o1.x, o2.x);
            }
        }
    };
    
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
