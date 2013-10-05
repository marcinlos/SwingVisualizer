package mlos.sgl.view;

import mlos.sgl.model.CanvasObject;
import mlos.sgl.util.GenericFactory;

public class GenericObjectViewFactory extends GenericFactory<ObjectView>
        implements ObjectViewFactory {

    @Override
    public ObjectView createView(CanvasObject object) {
        return create(object);
    }
}
