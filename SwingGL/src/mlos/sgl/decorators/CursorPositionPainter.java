package mlos.sgl.decorators;

import java.awt.Color;
import java.awt.Graphics2D;

import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.Painter;
import mlos.sgl.view.ScreenPoint;

public class CursorPositionPainter implements Painter {
    
    private final PropertyMap properties;
    
    public CursorPositionPainter(PropertyMap properties) {
        this.properties = properties;
    }

    @Override
    public void paint(CanvasPanel canvas, Graphics2D ctx) {
        ScreenPoint cursor = properties.get("cursor", ScreenPoint.class);
        if (cursor != null) {
            String text = "Pos: " + canvas.toVirtual(cursor);
            ctx.setColor(Color.black);
            ctx.drawString(text, 8, 15);

//            int size = 4;
//            Stroke s = new BasicStroke(1);
//            ctx.setStroke(s);
//            ctx.drawLine(cursor.x - size, cursor.y, cursor.x + size, cursor.y);
//            ctx.drawLine(cursor.x, cursor.y - size, cursor.x, cursor.y + size);
        }
    }

}