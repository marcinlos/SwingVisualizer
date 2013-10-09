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
    
    public static Rect leftBottomRightTop(double left, double bottom, 
            double right, double top) {
        return new Rect(left, bottom, right, top);
    }
    
    public static Rect leftBottomRightTop(Vec2d leftBottom, Vec2d rightTop) {
        return leftBottomRightTop(leftBottom.x, leftBottom.y, rightTop.x, 
                rightTop.y);
    }
    
    public static Rect centerSize(double x, double y, double width, double height) {
        return new Rect(x - width/2, y - height/2, x + width/2, y + height/2);
    }
    
    public static Rect leftBottomSize(double left, double bottom, double width, 
            double height) {
        return new Rect(left, bottom, left + width, bottom + height);
    }
    
    public static Rect aroundOrigin(double halfWidth, double halfHeight) {
        return leftBottomSize(-halfWidth, -halfHeight, 2 * halfWidth, 2 * halfHeight);
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
