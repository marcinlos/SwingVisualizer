package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import mlos.sgl.model.Canvas;
import mlos.sgl.model.CanvasObject;

public class CanvasPainter implements Painter {

    private static class ZComparator implements Comparator<CanvasObject> {
        @Override
        public int compare(CanvasObject o1, CanvasObject o2) {
            int res = Double.compare(o1.getZ(), o2.getZ());
            if (res != 0) {
                return res;
            } else {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }
    };

    private static final ZComparator COMPARATOR = new ZComparator();

    private final Canvas canvas;

    public CanvasPainter(Canvas canvas) {
        this.canvas = checkNotNull(canvas);
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        CanvasObjectPainter painter = new CanvasObjectPainter(panel, ctx);

        Set<CanvasObject> ordered = new TreeSet<>(COMPARATOR);
        ordered.addAll(canvas.getObjects());

        for (CanvasObject object : ordered) {
            object.accept(painter);
        }
    }

}
