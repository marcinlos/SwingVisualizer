#include "schewchuck.h"

double orient2dfast(double*, double*, double*);
double orient2dexact(double*, double*, double*);
double orient2dslow(double*, double*, double*);
void exactinit();


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