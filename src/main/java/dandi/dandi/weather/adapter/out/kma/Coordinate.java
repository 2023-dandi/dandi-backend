package dandi.dandi.weather.adapter.out.kma;

public class Coordinate {

    private final int nx;
    private final int ny;

    public Coordinate(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;
    }

    public int getNx() {
        return nx;
    }

    public int getNy() {
        return ny;
    }

    public boolean isInRange(Coordinate another, int range) {
        double maxDistanceInRange = Math.sqrt(Math.pow(range, 2) + Math.pow(range, 2));
        double distance = Math.sqrt(Math.pow(nx - another.nx, 2) + Math.pow(ny - another.ny, 2));
        return distance <= maxDistanceInRange;
    }
}
