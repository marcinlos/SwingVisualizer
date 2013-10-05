package mlos.sgl.ui;

import mlos.sgl.model.CanvasObject;
import mlos.sgl.view.ScreenPoint;

public interface ObjectGeometry {

    boolean hit(ScreenPoint point, int treshold);
    
    CanvasObject getObject();
    
}
