package mlos.sgl.view;

import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;

public class DefaultObjectViewFactory extends GenericObjectViewFactory {{
    
    register(CanvasPoint.class, PointPainter.class);
    register(CanvasSegment.class, SegmentPainter.class);
    
}}
