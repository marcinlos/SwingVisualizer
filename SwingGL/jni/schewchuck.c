#include "schewchuk.h"

double orient2dfast(double*, double*, double*);
double orient2dexact(double*, double*, double*);
double orient2dslow(double*, double*, double*);
double incircle(double*, double*, double*, double*);
void exactinit();


JNIEXPORT void JNICALL Java_mlos_sgl_core_Schewchuck_init(JNIEnv* env, jclass clazz) 
{
    exactinit();
}

JNIEXPORT jdouble JNICALL Java_mlos_sgl_core_Schewchuck_schewchuckOrient2d(
        JNIEnv* env, 
        jclass clazz, 
        jdouble ax, jdouble ay, 
        jdouble bx, jdouble by, 
        jdouble cx, jdouble cy)
{
    double aa[] = { ax, ay }, bb[] = { bx, by }, cc[] = { cx, cy };
    return orient2dexact(aa, bb, cc);
}

JNIEXPORT jdouble JNICALL Java_mlos_sgl_core_Schewchuck_schewchuckIncircle(JNIEnv* env, jclass clazz,
        jdouble ax, jdouble ay, jdouble bx, jdouble by, jdouble cx, jdouble cy, jdouble dx, jdouble dy) {
    double aa[] = { ax, ay }, bb[] = { bx, by }, cc[] = { cx, cy }, dd[] = { dx, dy };
    return incircle(aa, bb, cc, dd);
}
