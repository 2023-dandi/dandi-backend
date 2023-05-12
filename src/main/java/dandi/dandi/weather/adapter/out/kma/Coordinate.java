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
}
