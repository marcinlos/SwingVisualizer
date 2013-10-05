package mlos.sgl.view;

import mlos.sgl.core.Point;
import mlos.sgl.model.CanvasObject;
import mlos.sgl.model.CanvasPoint;
import mlos.sgl.model.CanvasSegment;
import mlos.sgl.model.CanvasVisitor;

public class HitTester {

    public static final int DEFAULT_TRESHOLD = 4;
    
    private final CanvasPanel canvasPanel;
    
    private final int treshold;
    
    private class Visitor implements CanvasVisitor {
        
        private boolean isHit;
        
        private ScreenPoint hit;
        
        public Visitor(ScreenPoint hit) {
            this.hit = hit;
        }
        
        public boolean isHit() {
            return isHit;
        }

        @Override
        public void visit(CanvasPoint point) {
            Point p = point.getPoint();
            ScreenPoint s = canvasPanel.toScreen(p);
            int distSq = ScreenPoint.distSq(hit, s);
            
            int r = point.getSize() / 2 + treshold;
            isHit = distSq < r * r;
        }

        @Override
        public void visit(CanvasSegment segment) {
            // TODO Auto-generated method stub
        }
        
    }
    
    public HitTester(CanvasPanel canvasPanel) {
        this(canvasPanel, DEFAULT_TRESHOLD);
    }
    
    public HitTester(CanvasPanel canvasPanel, int treshold) {
        this.canvasPanel = canvasPanel;
        this.treshold = treshold;
    }
    
    public boolean testHit(ScreenPoint point, CanvasObject object) {
        Visitor v = new Visitor(point);
        object.accept(v);
        return v.isHit();
    }


}
