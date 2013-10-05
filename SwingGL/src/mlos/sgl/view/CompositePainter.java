package mlos.sgl.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class CompositePainter implements Painter {
    
    private final List<Painter> painters = new ArrayList<>();

    @Override
    public void paint(CanvasPanel canvas, Graphics2D ctx) {
        for (Painter painter : painters) {
            painter.paint(canvas, ctx);
        }
    }
    
    public CompositePainter add(Painter painter) {
        painters.add(painter);
        return this;
    }

}
