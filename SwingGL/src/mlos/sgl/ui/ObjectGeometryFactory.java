package mlos.sgl.ui;

import mlos.sgl.model.CanvasObject;

public interface ObjectGeometryFactory {

    ObjectGeometry createGeometry(CanvasObject object);
    
}
