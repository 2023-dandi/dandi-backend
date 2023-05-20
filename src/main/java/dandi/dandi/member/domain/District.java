package dandi.dandi.member.domain;

public class District {

    private static final District INITIAL_DISTRICT = new District("서울특별시");

    private final String country;
    private final String city;
    private final String town;

    public District(String country, String city, String town) {
        this.country = country;
        this.city = city;
        this.town = town;
    }

    public District(String country, String city) {
        this(country, city, null);
    }

    public District(String country) {
        this(country, null, null);
    }

    public static District getInitialDistrict() {
        return INITIAL_DISTRICT;
    }
}
