package mlos.sgl.core;


public class Transform {
    
    private final double[][] m;
    
    public Transform() {
        this(new double[2][3]);
        m[0][0] = m[1][1] = 1;
    }
    
    private Transform(double[][] mat) {
        this.m = mat;
    }
    
    public Vec2d apply(Vec2d a) {
        double x = m[0][0] * a.x + m[0][1] * a.y + m[0][2];
        double y = m[1][0] * a.x + m[1][1] * a.y + m[1][2];
        return new Vec2d(x, y);
    }
    
    public Vec2d invert(Vec2d p) {
        double a = m[0][0];
        double b = m[0][1];
        double c = m[0][2];
        double d = m[1][0];
        double e = m[1][1];
        double f = m[1][2];
        
        double det = a * e - b * d;
        
        if (Math.abs(det) > 1e-20) {
            double m00 = e;
            double m01 = -b;
            double m02 = (b * f - c * e);
            
            double m10 = - d;
            double m11 = a;
            double m12 = (c * d - a * f);
            
            double x = m00 * p.x + m01 * p.y + m02;
            double y = m10 * p.x + m11 * p.y + m12;
            return new Vec2d(x / det, y / det);
        } else {
            throw new ArithmeticException("det = 0");
        }
    }
    
    @Override
    public String toString() {
        String rowFmt = String.format("{%1$s, %1$s, %1$s}", "%.2f");
        String fmt = String.format("{%1$s,\n %1$s}", rowFmt);
        return String.format(fmt,
                m[0][0], m[0][1], m[0][2],
                m[1][0], m[1][1], m[1][2]);
    }

    
    public static class Builder {
        
        private final double[][] m = new double[2][3];
        
        public Builder() {
            m[0][0] = m[1][1] = 1;
        }
        
        public Builder(Transform t) {
            copyMatrix(t.m, m);
        }
        
        private void copyMatrix(double[][] src, double[][] dst) {
            for (int i = 0; i < 2; ++ i) {
                System.arraycopy(src[i], 0, dst[i], 0, 3);
            }
        }
        
        public Builder apply(Transform t) {
            double[][] a = t.m;
            
            double m00 = a[0][0] * m[0][0] + a[0][1] * m[1][0];
            double m01 = a[0][0] * m[0][1] + a[0][1] * m[1][1];
            double m02 = a[0][0] * m[0][2] + a[0][1] * m[1][2] + a[0][2];
            
            double m10 = a[1][0] * m[0][0] + a[1][1] * m[1][0];
            double m11 = a[1][0] * m[0][1] + a[1][1] * m[1][1];
            double m12 = a[1][0] * m[0][2] + a[1][1] * m[1][2] + a[1][2];
            
            
            m[0][0] = m00; 
            m[0][1] = m01; 
            m[0][2] = m02;
            
            m[1][0] = m10; 
            m[1][1] = m11; 
            m[1][2] = m12;
            return this;
        }
        
        public Builder t(double dx, double dy) {
            m[0][2] += dx;
            m[1][2] += dy;
            return this;
        }
        
        public Builder tX(double dx) {
            return t(dx, 0);
        }
        
        public Builder tY(double dy) {
            return t(0, dy);
        }
        
        public Builder s(double sx, double sy) {
            m[0][0] *= sx;
            m[0][1] *= sx;
            m[0][2] *= sx;
            
            m[1][0] *= sy;
            m[1][1] *= sy;
            m[1][2] *= sy;
            return this;
        }
        
        public Builder sX(double sx) {
            return s(sx, 1);
        }
        
        public Builder sY(double sy) {
            return s(1, sy);
        }
        
        public Builder flipX() {
            return sX(-1);
        }
        
        public Builder flipY() {
            return sY(-1);
        }
        
        public Builder r(double theta) {
            double cos = Math.cos(theta);
            double sin = Math.sin(theta);
            
            double m00 = m[0][0];
            double m01 = m[0][1];
            double m02 = m[0][2];
            double m10 = m[1][0];
            double m11 = m[1][1];
            double m12 = m[1][2];
            
            m[0][0] = m00 * cos - m10 * sin;
            m[0][1] = m01 * cos - m11 * sin;
            m[0][2] = m02 * cos - m12 * sin;
            
            m[1][0] = m00 * sin + m10 * cos;
            m[1][1] = m01 * sin + m11 * cos;
            m[1][2] = m02 * sin + m12 * cos;
            
            return this;
        }
        
        public Builder invert() {
            double a = m[0][0];
            double b = m[0][1];
            double c = m[0][2];
            double d = m[1][0];
            double e = m[1][1];
            double f = m[1][2];
            
            double det = a * e - b * d;
            
            if (Math.abs(det) > 1e-20) {
                m[0][0] = e / det;
                m[0][1] = -b / det;
                m[0][2] = (b * f - c * e) / det;
                
                m[1][0] = - d / det;
                m[1][1] = a / det;
                m[1][2] = (c * d - a * f) / det;
            } else {
                throw new ArithmeticException("det = 0");
            }
            return this;
        }
        
        public Transform create() {
            double[][] copy = new double[2][3];
            copyMatrix(m, copy);
            return new Transform(copy);
        }
        
    }
    
}
