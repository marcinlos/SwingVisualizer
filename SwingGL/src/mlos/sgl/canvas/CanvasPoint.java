package mlos.sgl.canvas;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import mlos.sgl.core.Vec2d;

public class CanvasPoint extends CanvasObject {
    
    public static final Color DEFAULT_COLOR = Color.red;
    
    public static final Color DEFAULT_HOVER_COLOR = Color.orange;
    
    public static final Color DEFAULT_SELECTED_COLOR = Color.magenta;
    
    public static final Color DEFAULT_BORDER_COLOR = Color.black;

    public static final int DEFAULT_SIZE = 10;

    public static final int DEFAULT_BORDER_SIZE = 1;
    
    /** Position in virtual coordinates */
    private Vec2d point;
    
    /** Diameter of the point */
    private int size = DEFAULT_SIZE;

    /** Color of the point */
    private Color color = DEFAULT_COLOR;
    
    private Color hoverColor = DEFAULT_HOVER_COLOR;
    
    private Color selectedColor = DEFAULT_SELECTED_COLOR;
    
    private Color borderColor = DEFAULT_BORDER_COLOR; 

    private int borderSize = DEFAULT_BORDER_SIZE;

    public CanvasPoint() {
        this(Vec2d.ZERO);
    }

    public CanvasPoint(Vec2d point) {
        this(point, DEFAULT_Z);
    }

    public CanvasPoint(Vec2d point, double z) {
        super(z);
        setPoint(point);
    }
    

//    @Override
//    public void accept(CanvasVisitor visitor) {
//        visitor.visit(this);
//    }
    
    public Vec2d getPoint() {
        return point;
    }
    
    public synchronized void setPoint(Vec2d point) {
        this.point = checkNotNull(point);
        notifyListeners();
    }
    
    
    public synchronized int getSize() {
        return size;
    }

    public synchronized void setSize(int size) {
        checkArgument(size > 0, "Vec2d size must be positive");
        this.size = size;
        notifyListeners();
    }
    
    public synchronized Color getColor() {
        return color;
    }

    public synchronized void setColor(Color color) {
        this.color = checkNotNull(color);
        notifyListeners();
    }
    
    public synchronized Color getHoverColor() {
        return hoverColor;
    }

    public synchronized void setHoverColor(Color hoverColor) {
        this.hoverColor = checkNotNull(hoverColor);
        notifyListeners();
    }

    public synchronized Color getSelectedColor() {
        return selectedColor;
    }

    public synchronized void setSelectedColor(Color selectedColor) {
        this.selectedColor = checkNotNull(selectedColor);
        notifyListeners();
    }
    
    public synchronized Color getBorderColor() {
        return borderColor;
    }

    public synchronized void setBorderColor(Color borderColor) {
        this.borderColor = checkNotNull(borderColor);
        notifyListeners();
    }

    public synchronized int getBorderSize() {
        return borderSize;
    }

    public synchronized void setBorderSize(int borderSize) {
        checkArgument(borderSize >= 0, "Border size must be non-negative");
        this.borderSize = borderSize;
    }

}
