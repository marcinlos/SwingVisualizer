package mlos.sgl.view;

import mlos.sgl.canvas.CanvasPoint;

public class DefaultObjectViewFactory extends GenericObjectViewFactory {{
    
    register(CanvasPoint.class, PointView.class);
    
}}
