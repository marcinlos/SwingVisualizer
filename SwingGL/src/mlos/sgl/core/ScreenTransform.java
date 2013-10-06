package mlos.sgl.core;


public interface ScreenTransform {
    /**
     * Transforms the virtual coordinates to the screen coordinates.
     * 
     * @param p
     *            ScreenPoint in virtual coordinates
     * @return Point in screen coordinates
     */
    Point toScreen(Point p);

    /**
     * Transforms the virtual coordinates to screen coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return Point in screen coordinates
     */
    Point toScreen(double x, double y);

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return ScreenPoint in virtual coordinates
     */
    Point toVirtual(double x, double y);

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param p
     *            ScreenPoint in screen coordinates
     * @return ScreenPoint in virtual coordinates
     */
    Point toVirtual(Point p);
}
