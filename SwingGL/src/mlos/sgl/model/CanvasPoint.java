package mlos.sgl.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import mlos.sgl.core.Point;

public class CanvasPoint extends CanvasObject {
    
    public static final Color DEFAULT_COLOR = Color.red;
    
    public static final Color DEFAULT_HOVER_COLOR = Color.blue;
    
    public static final Color DEFAULT_SELECTED_COLOR = Color.green;
    
    public static final Color DEFAULT_BORDER_COLOR = Color.black;

    public static final int DEFAULT_SIZE = 15;

    public static final int DEFAULT_BORDER_SIZE = 2;
    
    /** Position in virtual coordinates */
    private Point point;
    
    /** Diameter of the point */
    private int size = DEFAULT_SIZE;

    /** Color of the point */
    private Color color = DEFAULT_COLOR;
    
    private Color hoverColor = DEFAULT_HOVER_COLOR;
    
    private Color selectedColor = DEFAULT_SELECTED_COLOR;
    
    private Color borderColor = DEFAULT_BORDER_COLOR; 

    private int borderSize = DEFAULT_BORDER_SIZE;
    

    public CanvasPoint(Point point) {
        this(point, DEFAULT_Z);
    }

    public CanvasPoint(Point point, double z) {
        super(z);
        setPoint(point);
    }
    

    @Override
    public void accept(CanvasVisitor visitor) {
        visitor.visit(this);
    }
    
    public Point getPoint() {
        return point;
    }
    
    public void setPoint(Point point) {
        this.point = checkNotNull(point);
        notifyListeners();
    }
    
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        checkArgument(size > 0, "Point size must be positive");
        this.size = size;
        notifyListeners();
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = checkNotNull(color);
        notifyListeners();
    }
    
    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = checkNotNull(hoverColor);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = checkNotNull(selectedColor);
    }
    
    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = checkNotNull(borderColor);
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        checkArgument(borderSize > 0, "Border size must be positive");
        this.borderSize = borderSize;
    }

}
