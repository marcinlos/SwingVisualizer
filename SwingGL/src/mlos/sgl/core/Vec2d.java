package mlos.sgl.core;

import java.util.Objects;

public final class Vec2d {
    
    public final double x;
    public final double y;

    public Vec2d() {
        this(0, 0);
    }
    
    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
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
    
}
