package mlos.sgl.view;

import mlos.sgl.model.CanvasObject;

public interface ViewFactory {

    View createView(CanvasObject object);
    
}
