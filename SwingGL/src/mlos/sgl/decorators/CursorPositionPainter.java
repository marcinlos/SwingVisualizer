package mlos.sgl.decorators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.Painter;

public class CursorPositionPainter implements Painter {
    
    private final PropertyMap properties;
    
    public CursorPositionPainter(PropertyMap properties) {
        this.properties = properties;
    }

    @Override
    public void paint(Transform toScreen, Graphics2D ctx) {
        Vec2d cursor = properties.get("cursor", Vec2d.class);
        if (cursor != null) {
            String text = "Pos: " + toScreen.invert(cursor);
            ctx.setColor(Color.black);
            ctx.drawString(text, 8, 15);

            int size = 4;
            Stroke s = new BasicStroke(1);
            ctx.setStroke(s);
            
            int cx = (int) cursor.x;
            int cy = (int) cursor.y;
            ctx.drawLine(cx - size, cy, cx + size, cy);
            ctx.drawLine(cx, cy - size, cx, cy + size);
        }
    }

}