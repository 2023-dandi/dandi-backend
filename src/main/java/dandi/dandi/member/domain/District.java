package dandi.dandi.member.domain;

import java.util.Objects;

public class District {

    private static final District INITIAL_DISTRICT = new District("서울특별시");

    private final String first;
    private final String second;
    private final String third;

    public District(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public District(String first, String second) {
        this(first, second, null);
    }

    public District(String first) {
        this(first, null, null);
    }

    public static District getInitialDistrict() {
        return INITIAL_DISTRICT;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return Objects.equals(first, district.first) && Objects.equals(second, district.second) && Objects.equals(third, district.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
