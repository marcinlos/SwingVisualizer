package mlos.sgl.view;

import mlos.sgl.model.CanvasObject;

public interface ObjectViewFactory {

    ObjectView createView(CanvasObject object);
    
}
