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
     * Transforms the virtual coordinates to screen coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return Vec2d in screen coordinates
     */
    Vec2d toScreen(double x, double y);

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return ScreenPoint in virtual coordinates
     */
    Vec2d toVirtual(double x, double y);

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param p
     *            ScreenPoint in screen coordinates
     * @return ScreenPoint in virtual coordinates
     */
    Vec2d toVirtual(Vec2d p);
}
