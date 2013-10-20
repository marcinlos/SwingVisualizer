package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.neg;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.ObjectZComparator;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;

public class CanvasView implements Painter {

    private static class ZComparator implements Comparator<ObjectPainter> {

        @Override
        public int compare(ObjectPainter a, ObjectPainter b) {
            CanvasObject aa = a.getObject();
            CanvasObject bb = b.getObject();
            return ObjectZComparator.INSTANCE.compare(aa, bb);
        }
        
    };

    private static final ZComparator COMPARATOR = new ZComparator();

    private final Collection<ObjectPainter> objects = new HashSet<>();

    private final CanvasPanel panel;
    
    private final List<Painter> prePainters = new CopyOnWriteArrayList<>();
    private final List<Painter> postPainters = new CopyOnWriteArrayList<>();

    private Transform planeToNorm = new Transform();
    
    public CanvasView(CanvasPanel panel) {
        this.panel = checkNotNull(panel);
        panel.setPainter(this);
    }
    
    public synchronized void setTransform(Transform transform) {
        this.planeToNorm = transform;
        refresh();
    }
    
    public void setViewport(Rect rect) {
        Transform t = new Transform.Builder()
            .t(neg(rect.center()))
            .s(2 / rect.width(), 2 / rect.height())
            .create();
        setTransform(t);
    }
    
    public void setViewport(double w, double h) {
        setViewport(Rect.at(0, 0, w, h));
    }
    
    public Transform planeToNorm() {
        return planeToNorm;
    }
    
    public Transform normToScreen() {
        return panel.normToScreen();
    }
    
    public void append(Transform t) {
        setTransform(Transforms.compose(planeToNorm, t));
    }
    
    public void prepend(Transform t) {
        setTransform(Transforms.compose(t, planeToNorm));
    }
    
    public Transform planeToScreen() {
        Transform normToScreen = panel.normToScreen();
        return Transforms.compose(planeToNorm, normToScreen);
    }
    
    public void refresh() {
        panel.refresh();
    }
    
    public void addPrePainter(Painter painter) {
        prePainters.add(checkNotNull(painter));
    }
    
    public void removePrePainter(Painter painter) {
        prePainters.remove(painter);
    }
    
    public void addPostPainter(Painter painter) {
        postPainters.add(checkNotNull(painter));
    }
    
    public void removePostPainter(Painter painter) {
        postPainters.remove(painter);
    }

    public synchronized boolean add(ObjectPainter painter) {
        return objects.add(painter);
    }
    
    public synchronized boolean remove(ObjectPainter painter) {
        return objects.remove(painter);
    }

    @Override
    public synchronized void paint(Transform toScreen, Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Transform t = planeToScreen();
    
        for (Painter p : prePainters) {
            p.paint(t, ctx);
        }
        
        final List<ObjectPainter> zsorted = new ArrayList<>(objects);
        Collections.sort(zsorted, COMPARATOR);
        
        for (ObjectPainter object : zsorted) {
            object.paint(t, ctx);
        }
        
        for (Painter p : postPainters) {
            p.paint(t, ctx);
        }
        
    }
    
    
}
