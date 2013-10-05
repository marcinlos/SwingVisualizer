package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class CanvasPainter implements Painter {

    private static class ZComparator implements Comparator<ObjectView> {
        @Override
        public int compare(ObjectView a, ObjectView b) {
            int res = Double.compare(a.getObject().getZ(), b.getObject().getZ());
            if (res != 0) {
                return res;
            } else {
                return Integer.compare(a.hashCode(), b.hashCode());
            }
        }
    };

    private static final ZComparator COMPARATOR = new ZComparator();

    private final Collection<ObjectView> views;

    public CanvasPainter(Collection<ObjectView> canvas) {
        this.views = checkNotNull(canvas);
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        Set<ObjectView> ordered = new TreeSet<>(COMPARATOR);
        ordered.addAll(views);

        for (ObjectView view : ordered) {
            view.paint(panel, ctx);
        }
    }

}
