package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;

public interface ObjectGeometryFactory {

    ObjectGeometry createGeometry(CanvasObject object);
    
}
