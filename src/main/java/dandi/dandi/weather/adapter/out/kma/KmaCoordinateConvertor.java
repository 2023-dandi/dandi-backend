package dandi.dandi.weather.adapter.out.kma;

import org.springframework.stereotype.Component;

@Component
public class KmaCoordinateConvertor {

    private static final double PI = 3.141592653589793;
    private static final double DEGRAD = 0.017453292519943295;
    private static final double RE = 1274.2017540000002;
    private static final double OLON = 2.199114857512855;
    private static final double SN = 0.715566847180628;
    private static final double SF = 1.7930256259404227;
    private static final double RO = 1366.777782832094;
    private static final double XO = 42.0;
    private static final double YO = 135.0;


    public Coordinate convert(double latitude, double longitude) {
        double ra = Math.tan(PI * 0.25 + latitude * DEGRAD * 0.5);
        ra = RE * SF / Math.pow(ra, SN);
        double theta = longitude * DEGRAD - OLON;

        if (theta > PI) {
            theta -= 2.0 * PI;
        }

        if (theta < -PI) {
            theta += 2.0 * PI;
        }

        theta *= SN;

        double x = ra * Math.sin(theta) + XO;
        double y = RO - ra * Math.cos(theta) + YO;

        return new Coordinate((int) (x + 1.5), (int) (y + 1.5));
    }
}
