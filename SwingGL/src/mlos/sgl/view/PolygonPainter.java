package mlos.sgl.view;

import java.awt.Graphics2D;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Transform;

public class PolygonPainter implements ObjectPainter {
    
    private final CanvasPolygon polygon;

    public PolygonPainter(CanvasPolygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public void paint(Transform toScreen, Graphics2D ctx) {
        new Drawer(ctx, toScreen)
            .solid(2)
            .polygon(polygon.getPoints())
            .restore();
    }

    @Override
    public CanvasObject getObject() {
        return polygon;
    }

}
