package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.ScreenTransform;
import mlos.sgl.view.ScreenPoint;

public interface ObjectGeometry {

    boolean hit(ScreenPoint p, ScreenTransform transform, int treshold); 
    
    CanvasObject getObject();
    
}
