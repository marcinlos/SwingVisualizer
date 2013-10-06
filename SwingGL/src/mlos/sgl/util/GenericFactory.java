package mlos.sgl.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class GenericFactory<T> {
    
    private final Map<Class<?>, Class<? extends T>> impls = new HashMap<>();

    public T create(Object object) {
        Class<?> clazz = object.getClass();
        Class<? extends T> viewClass = impls.get(clazz);
        if (viewClass != null) {
            Constructor<? extends T> ctor = getCtor(viewClass, clazz);
            try {
                return ctor.newInstance(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
    
    public void register(Class<?> clazz, Class<? extends T> view) {
        impls.put(clazz, view);
    }

    private static <T> Constructor<T> getCtor(Class<T> view, Class<?> clazz) {
        try {
            return view.getConstructor(clazz);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
}
