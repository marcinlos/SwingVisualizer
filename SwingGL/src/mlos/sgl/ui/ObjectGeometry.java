package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public interface ObjectGeometry {

    boolean hit(Vec2d p, Transform screen, int treshold); 
    
    CanvasObject getObject();
    
}
