package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.ObjectZComparator;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 14;

    private final class MotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            update(e);
            onMouseHover(getPosition(e));
        }

        private void update(MouseEvent e) {
            properties.put("cursor", getPosition(e));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            update(e);
            Vec2d p = getPosition(e);
            int dx = (int) (p.x - dragBase.x);
            int dy = (int) (p.y - dragBase.y);
            if (captured != null) {
//                captured.
            }
        }
    }
    
    private final class ButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                Collection<CanvasObject> hits = findHits(getPosition(e));
                if (! hits.isEmpty()) {
                    captured = hits.iterator().next();
                    captured.setSelected(true);
                    panel.refresh();
                }
            }
            dragBase = getPosition(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            properties.remove("cursor");
        }
        
    }
    
    private Vec2d getPosition(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }
    
    private final CanvasPanel panel;
    
    private final PropertyMap properties;

    private ObjectGeometryFactory geometryFactory;

    private final Map<CanvasObject, ObjectGeometry> geometryMap = new HashMap<>();
    
    
    private Vec2d dragBase;
    
    private CanvasObject captured;

    public CanvasController(CanvasPanel panel, PropertyMap properties, 
            ObjectGeometryFactory geometryFactory) {
        this.panel = checkNotNull(panel);
        this.properties = checkNotNull(properties);
        this.geometryFactory = checkNotNull(geometryFactory);
        
        panel.addMouseMotionListener(new MotionListener());
        panel.addMouseListener(new ButtonListener());
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectGeometry geometry = geometryFactory.createGeometry(object);
        geometryMap.put(object, geometry);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        geometryMap.remove(object);
    }
    
    
    void onMouseHover(Vec2d p) {
        for (CanvasObject object : geometryMap.keySet()) {
            object.setHover(false);
        }
        Collection<CanvasObject> hits = findHits(p);
        for (CanvasObject object : hits) {
            object.setHover(true);
        }
    }
    
    public Collection<CanvasObject> findHits(Vec2d p) {
        Set<CanvasObject> hits = new TreeSet<>(ObjectZComparator.INSTANCE);
        for (ObjectGeometry geometry : geometryMap.values()) {
            if (geometry.hit(p, panel, DEFAULT_TRESHOLD)) {
                hits.add(geometry.getObject());
            }
        }
        return hits;
    }

}
