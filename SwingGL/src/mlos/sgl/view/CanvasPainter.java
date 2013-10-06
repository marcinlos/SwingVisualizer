package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.ObjectZComparator;

public class CanvasPainter implements Painter, CanvasListener {

    private static class ZComparator implements Comparator<ObjectView> {

        @Override
        public int compare(ObjectView a, ObjectView b) {
            CanvasObject aa = a.getObject();
            CanvasObject bb = b.getObject();
            return ObjectZComparator.INSTANCE.compare(aa, bb);
        }
    };

    private static final ZComparator COMPARATOR = new ZComparator();

    private final Map<CanvasObject, ObjectView> viewMap = new HashMap<>();

    private final Collection<ObjectView> views = new TreeSet<>(COMPARATOR);

    private ObjectViewFactory viewFactory;

    
    public CanvasPainter(ObjectViewFactory viewFactory) {
        this.viewFactory = checkNotNull(viewFactory);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectView view = viewFactory.createView(object);
        viewMap.put(object, view);
        views.add(view);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        ObjectView view = viewMap.remove(object);
        views.remove(view);
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (ObjectView view : views) {
            view.paint(panel, ctx);
        }
    }
}
