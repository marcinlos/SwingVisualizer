package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;

import mlos.sgl.model.Canvas;
import mlos.sgl.model.CanvasObject;

public class CanvasPainter implements Painter {
    
    private final Canvas canvas;

    public CanvasPainter(Canvas canvas) {
        this.canvas = checkNotNull(canvas);
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        CanvasObjectPainter painter = new CanvasObjectPainter(panel, ctx);
        
        for (CanvasObject object : canvas.getObjects()) {
            object.accept(painter);
        }
    }

}
