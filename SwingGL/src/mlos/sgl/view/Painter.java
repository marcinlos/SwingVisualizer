package mlos.sgl.view;

import java.awt.Graphics2D;

import mlos.sgl.core.Transform;

public interface Painter {
    
    void paint(Transform normToScreen, Graphics2D ctx);

}
