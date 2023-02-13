package dandi.dandi.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Latitude {

    private static final double MIN_VALUE = -90.0;
    private static final double MAX_VALUE = 90.0;

    @Column(name = "latitude")
    private double value;

    private Latitude() {
    }

    public Latitude(double value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(double value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("위도 범위가 잘못되었습니다.");
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
        if (!(o instanceof Latitude)) {
            return false;
        }
        Latitude latitude = (Latitude) o;
        return Double.compare(latitude.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
