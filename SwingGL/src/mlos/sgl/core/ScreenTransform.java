package mlos.sgl.core;


public interface ScreenTransform {
    
    /**
     * Transforms the virtual coordinates to the screen coordinates.
     * 
     * @param p
     *            ScreenPoint in virtual coordinates
     * @return Vec2d in screen coordinates
     */
    Vec2d toScreen(Vec2d p);

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param p
     *            ScreenPoint in screen coordinates
     * @return ScreenPoint in virtual coordinates
     */
    Vec2d toVirtual(Vec2d p);
}
