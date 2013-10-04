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
    
    public static Rect leftBottomRightTop(Point leftBottom, Point rightTop) {
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
    
    public Point center() {
        return new Point((left + right) / 2, (top + bottom) / 2);
    }
    
    public Point leftTop() {
        return new Point(left, top);
    }
    
    public Point leftBottom() {
        return new Point(left, bottom);
    }
    
    public Point rightTop() {
        return new Point(right, top);
    }
    
    public Point rightBottom() {
        return new Point(right, bottom);
    }
    
    
}
