package mlos.sgl.canvas;

public interface CanvasVisitor {

    void visit(CanvasPoint point);
    
    void visit(CanvasSegment segment);
    
}
