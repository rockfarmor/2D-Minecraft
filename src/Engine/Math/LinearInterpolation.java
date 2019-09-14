package Engine.Math;

public class LinearInterpolation extends AbstractInterpolation {

    /**
     * Constructor.
     */
    public LinearInterpolation(double[] x, double[] y) {
        super(x, y);
    }

    @Override
    double rawinterp(int j, double x) {
        if (xx[j] == xx[j + 1]) {
            return yy[j];
        } else {
            return yy[j] + ((x - xx[j]) / (xx[j + 1] - xx[j])) * (yy[j + 1] - yy[j]);
        }
    }
}
