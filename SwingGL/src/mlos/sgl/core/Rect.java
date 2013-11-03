package mlos.sgl.core;

import static com.google.common.base.Preconditions.checkArgument;

public final class Rect {
    
    private final double left;
    private final double bottom;
    private final double right;
    private final double top;
    
    private Rect(double left, double bottom, double right, double top) {
        checkArgument(left <= right, "Left > right (%d > %d)", left, right);
        checkArgument(bottom <= top, "Bottom > top (%d > %d)", bottom, top);
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }
    
    public static Rect bounds(double left, double bottom, 
            double right, double top) {
        return new Rect(left, bottom, right, top);
    }
    
    public static Rect bounds(Vec2d leftBottom, Vec2d rightTop) {
        return bounds(leftBottom.x, leftBottom.y, rightTop.x, rightTop.y);
    }
    
    public static Rect at(double x, double y, double w, double h) {
        return new Rect(x - w/2, y - h/2, x + w/2, y + h/2);
    }
    
    public static Rect at(Vec2d center, double w, double h) {
        return at(center.x, center.y, w, h);
    }
    
    public static Rect lbSize(double left, double bottom, double w, double h) {
        return new Rect(left, bottom, left + w, bottom + h);
    }
    
    public static Rect aroundOrigin(double halfWidth, double halfHeight) {
        return lbSize(-halfWidth, -halfHeight, 2 * halfWidth, 2 * halfHeight);
    }
    
    public static Rect aroundOrigin(double extent) {
        return aroundOrigin(extent, extent);
    }
    
    public static Rect expand(Rect r, double dx, double dy) {
        return at(r.center(), r.width() + dx, r.height() + dy);
    }
    
    public static Rect scale(Rect r, double sx, double sy) {
        Vec2d c = r.center();
        return at(c, r.width() * sx, r.width() * sy);
    }
    
    public double left() {
        return left;
    }
    
    public double right() {
        return right;
    }
    
    public double bottom() {
        return bottom;
    }
    
    public double top() {
        return top;
    }

    public double width() {
        return right() - left();
    }
    
    public double height() {
        return top() - bottom();
    }
    
    public Vec2d center() {
        return new Vec2d((left + right) / 2, (top + bottom) / 2);
    }
    
    public Vec2d leftTop() {
        return new Vec2d(left, top);
    }
    
    public Vec2d leftBottom() {
        return new Vec2d(left, bottom);
    }
    
    public Vec2d rightTop() {
        return new Vec2d(right, top);
    }
    
    public Vec2d rightBottom() {
        return new Vec2d(right, bottom);
    }
    
    
}
