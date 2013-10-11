package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;

public interface ObjectControllerFactory {

    ObjectController createGeometry(CanvasObject object);
    
}
