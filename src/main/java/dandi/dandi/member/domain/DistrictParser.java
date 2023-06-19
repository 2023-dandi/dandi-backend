package dandi.dandi.member.domain;

import org.springframework.stereotype.Component;

@Component
public class DistrictParser {

    private static final String DISTRICT_DELIMITER = " ";
    private static final int COUNTRY_INDEX = 0;
    private static final int CITY_INDEX = 1;
    private static final int TOWN_INDEX = 2;

    public District parse(String value) {
        String[] districts = value.split(DISTRICT_DELIMITER);
        if (districts.length == 1) {
            return new District(districts[COUNTRY_INDEX]);
        } else if (districts.length == 2) {
            return new District(districts[COUNTRY_INDEX], districts[CITY_INDEX]);
        }
        return new District(districts[COUNTRY_INDEX], districts[CITY_INDEX], districts[TOWN_INDEX]);
    }
}
