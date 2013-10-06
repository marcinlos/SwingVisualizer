package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Vec2d;
import mlos.sgl.core.ScreenTransform;

public interface ObjectGeometry {

    boolean hit(Vec2d p, ScreenTransform transform, int treshold); 
    
    CanvasObject getObject();
    
}
