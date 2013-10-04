package mlos.sgl.model;

public interface CanvasVisitor {

    void visit(CanvasPoint point);
    
    void visit(CanvasSegment segment);
    
}
