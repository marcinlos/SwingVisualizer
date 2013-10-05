package mlos.sgl.view;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.model.CanvasObject;

class GenericViewFactory implements ViewFactory {
    
    private final Map<Class<?>, Class<? extends View>> views = new HashMap<>();

    @Override
    public View createView(CanvasObject object) {
        Class<?> clazz = object.getClass();
        Class<? extends View> viewClass = views.get(clazz);
        Constructor<? extends View> ctor = getCtor(viewClass, clazz);
        try {
            return ctor.newInstance(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void register(Class<? extends CanvasObject> clazz, 
            Class<? extends View> view) {
        views.put(clazz, view);
    }

    private static <T> Constructor<T> getCtor(Class<T> view, Class<?> clazz) {
        try {
            return view.getConstructor(clazz);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
}
