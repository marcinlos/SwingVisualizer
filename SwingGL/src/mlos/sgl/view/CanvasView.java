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
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;

public class CanvasView implements Painter, CanvasListener {

    private static class ZComparator implements Comparator<ObjectPainter> {

        @Override
        public int compare(ObjectPainter a, ObjectPainter b) {
            CanvasObject aa = a.getObject();
            CanvasObject bb = b.getObject();
            return ObjectZComparator.INSTANCE.compare(aa, bb);
        }
        
    };

    private static final ZComparator COMPARATOR = new ZComparator();

    private final Map<CanvasObject, ObjectPainter> viewMap = new HashMap<>();

    private final Collection<ObjectPainter> views = new TreeSet<>(COMPARATOR);

    private ObjectPainterFactory viewFactory;
    
    

    private Transform planeToNorm = new Transform();
    
    public CanvasView(ObjectPainterFactory viewFactory) {
        this.viewFactory = checkNotNull(viewFactory);
    }
    
    public void setTransform(Transform transform) {
        this.planeToNorm = transform;
    }
    
    public Transform getTransform() {
        return planeToNorm;
    }
    
    public void apply(Transform t) {
        planeToNorm = Transforms.compose(planeToNorm, t);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectPainter view = viewFactory.createPainter(object);
        viewMap.put(object, view);
        views.add(view);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        ObjectPainter view = viewMap.remove(object);
        views.remove(view);
    }

    @Override
    public void paint(Transform normToScreen, Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Transform t = Transforms.compose(planeToNorm, normToScreen);
        
        for (ObjectPainter view : views) {
            view.paint(t, ctx);
        }
    }
}
