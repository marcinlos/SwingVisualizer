package mlos.sgl.core;

import java.util.Objects;

public final class Point {
    
    public final double x;
    public final double y;

    public Point() {
        this(0, 0);
    }
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point other = (Point) o;
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
        return String.format("(%d, %d)", x, y);
    }

}
