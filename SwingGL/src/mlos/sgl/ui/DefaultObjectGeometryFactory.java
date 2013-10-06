package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;

public class DefaultObjectGeometryFactory extends GenericObjectGeometryFactory {{

    register(CanvasPoint.class, PointGeometry.class);
    register(CanvasSegment.class, SegmentGeometry.class);

}}
