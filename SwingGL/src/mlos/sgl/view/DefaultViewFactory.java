package mlos.sgl.view;

import mlos.sgl.model.CanvasPoint;

public class DefaultViewFactory extends GenericViewFactory {

    public DefaultViewFactory() {
        register(CanvasPoint.class, PointView.class);
    }

}
