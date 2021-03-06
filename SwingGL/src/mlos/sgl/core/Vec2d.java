package mlos.sgl.core;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.Objects;

public final class Vec2d implements Transformable<Vec2d> {
    
    public final double x;
    public final double y;
    public static final Vec2d ZERO = new Vec2d();

    public Vec2d() {
        this(0, 0);
    }
    
    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public static Vec2d polar(double r, double theta) {
        double x = r * cos(theta);
        double y = r * sin(theta);
        return new Vec2d(x, y);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec2d) {
            Vec2d other = (Vec2d) o;
            return x == other.x && y == other.y;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }

    @Override
    public Vec2d apply(Transform t) {
        return t.apply(this);
    }
    
}
