package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;

public class DefaultObjectControllerFactory extends GenericObjectControllerFactory {{

    register(CanvasPoint.class, PointController.class);
    register(CanvasSegment.class, SegmentController.class);

}}
