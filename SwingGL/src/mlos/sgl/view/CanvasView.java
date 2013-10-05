package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.model.Canvas;
import mlos.sgl.model.CanvasListener;
import mlos.sgl.model.CanvasObject;

public class CanvasView implements Painter, CanvasListener {
    
    private final Canvas canvas;
    
    private final Painter painter;
    
    private ViewFactory viewFactory;
    
    private final Map<CanvasObject, View> viewMap = new HashMap<>();
    

    public CanvasView(Canvas canvas, ViewFactory viewFactory) {
        this.canvas = checkNotNull(canvas);
        this.viewFactory = checkNotNull(viewFactory);
        this.painter = createPainter();
        canvas.addListener(this);
    }

    private Painter createPainter() {
        Collection<View> views = viewMap.values();
        return new CanvasPainter(Collections.unmodifiableCollection(views));
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        painter.paint(panel, ctx);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        View view = viewFactory.createView(object);
        viewMap.put(object, view);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        viewMap.remove(object);
    }
    
}
