package mlos.sgl.view;

import mlos.sgl.model.CanvasPoint;

public class DefaultObjectViewFactory extends GenericObjectViewFactory {{
    
    register(CanvasPoint.class, PointView.class);
    
}}
