package mlos.sgl.util;

import static mlos.sgl.core.Geometry.lerp;
import static mlos.sgl.core.Geometry.lerpPoly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mlos.sgl.core.Rect;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

import com.google.common.base.Predicate;

public final class Randomizer {

    private Randomizer() {
        // non-instantiable
    }

    private interface Source<T> {
        T next();
    }

    private abstract static class RandomSource<T> implements Source<T> {
        protected final Random rand = new Random();
    }

    private static class FilteredSource<T> implements Source<T> {

        private final Source<T> source;
        private final Predicate<? super T> predicate;
        private final int treshold;

        public FilteredSource(Source<T> source, Predicate<? super T> predicate,
                int treshold) {
            this.source = source;
            this.predicate = predicate;
            this.treshold = treshold;
        }

        public FilteredSource(Source<T> source, Predicate<? super T> predicate) {
            this(source, predicate, 1000);
        }

        @Override
        public T next() {
            for (int i = 0; i < treshold; ++i) {
                T value = source.next();
                if (predicate.apply(value)) {
                    return value;
                }
            }
            throw new RuntimeException("Generator gave up after " + treshold
                    + " attempts");
        }

    }

    public static class Generator<T> {

        protected final Source<T> source;

        private Generator(Source<T> source) {
            this.source = source;
        }

        public T one() {
            return source.next();
        }

        public List<T> list(int n) {
            List<T> points = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) {
                points.add(source.next());
            }
            return points;
        }

        public Generator<T> filter(Predicate<? super T> predicate) {
            Source<T> src = new FilteredSource<T>(source, predicate);
            return new Generator<>(src);
        }

    }
    
    public static class PointGenerator extends Generator<Vec2d> {
        
        private PointGenerator(Source<Vec2d> source) {
            super(source);
        }
        
        public Generator<Segment> segments() {
            Source<Segment> source = new Source<Segment>() {
                @Override
                public Segment next() {
                    return new Segment(one(), one());
                }
            };
            return new Generator<>(source);
        }

    }

    public static PointGenerator inRect(final Rect rect) {
        Source<Vec2d> source = new RandomSource<Vec2d>() {

            @Override
            public Vec2d next() {
                double s = rand.nextDouble();
                double t = rand.nextDouble();

                double x = lerp(s, rect.left(), rect.right());
                double y = lerp(t, rect.bottom(), rect.top());
                return new Vec2d(x, y);
            }
        };
        return new PointGenerator(source);
    }

    public static PointGenerator inSquare(double r) {
        Rect rect = Rect.aroundOrigin(r, r);
        return inRect(rect);
    }
    
    public static PointGenerator onPoly(final Vec2d... points) {
        Source<Vec2d> source = new RandomSource<Vec2d>() {
            @Override
            public Vec2d next() {
                double t = rand.nextDouble() * (points.length - 1);
                return lerpPoly(t, points);
            }
        };
        return new PointGenerator(source);
    }

    public static PointGenerator onCircle(final double r) {
        Source<Vec2d> source = new RandomSource<Vec2d>() {

            @Override
            public Vec2d next() {
                double theta = rand.nextDouble() * 2 * Math.PI;
                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);
                return new Vec2d(x, y);
            }
        };
        return new PointGenerator(source);
    }

    public static PointGenerator onSegment(final Segment seg) {
        Source<Vec2d> source = new RandomSource<Vec2d>() {

            @Override
            public Vec2d next() {
                double t = rand.nextDouble();
                return lerp(t, seg);
            }
        };
        return new PointGenerator(source);
    }
    
    public static PointGenerator onSegment(Vec2d a, Vec2d b) {
        return onSegment(new Segment(a, b));
    }

}
