package dandi.dandi.weather.adapter.out.kma;

import java.util.Objects;

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

    public boolean isCloserThan(Coordinate another, double distance) {
        double distanceBetween = Math.sqrt(Math.pow(nx - another.nx, 2) + Math.pow(ny - another.ny, 2));
        return distanceBetween <= distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return nx == that.nx && ny == that.ny;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nx, ny);
    }
}
