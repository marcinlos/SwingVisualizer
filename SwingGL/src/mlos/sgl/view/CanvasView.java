package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;

public class CanvasView implements Painter, CanvasListener {
    
    
    private final Painter painter;
    
    private ObjectViewFactory viewFactory;
    
    private final Map<CanvasObject, ObjectView> viewMap = new HashMap<>();
    

    public CanvasView(ObjectViewFactory viewFactory) {
        this.viewFactory = checkNotNull(viewFactory);
        this.painter = createPainter();
    }

    private Painter createPainter() {
        Collection<ObjectView> views = viewMap.values();
        return new CanvasPainter(Collections.unmodifiableCollection(views));
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        painter.paint(panel, ctx);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectView view = viewFactory.createView(object);
        viewMap.put(object, view);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        viewMap.remove(object);
    }
    
}
