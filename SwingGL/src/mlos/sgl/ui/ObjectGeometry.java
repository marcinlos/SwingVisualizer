package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Point;
import mlos.sgl.core.ScreenTransform;

public interface ObjectGeometry {

    boolean hit(Point p, ScreenTransform transform, int treshold); 
    
    CanvasObject getObject();
    
}
