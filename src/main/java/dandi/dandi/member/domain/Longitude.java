package dandi.dandi.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Longitude {

    private static final double MIN_VALUE = -180.0;
    private static final double MAX_VALUE = 180.0;

    @Column(name = "longitude")
    private double value;

    private Longitude() {
    }

    public Longitude(double value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(double value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("경도 범위가 잘못되었습니다.");
        }
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Longitude)) {
            return false;
        }
        Longitude longitude = (Longitude) o;
        return Double.compare(longitude.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
