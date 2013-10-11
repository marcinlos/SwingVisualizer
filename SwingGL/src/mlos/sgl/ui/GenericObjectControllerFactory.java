package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.util.GenericFactory;

public class GenericObjectControllerFactory extends
        GenericFactory<ObjectController> implements ObjectControllerFactory {

    @Override
    public ObjectController createController(CanvasObject object) {
        return create(object);
    }

}
