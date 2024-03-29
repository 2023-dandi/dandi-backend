package dandi.dandi.member.domain;

import org.springframework.stereotype.Component;

@Component
public class DistrictParser {

    private static final String DISTRICT_DELIMITER = " ";
    private static final int FIRST_DISTRICT_INDEX = 0;
    private static final int SECOND_DISTRICT_INDEX = 1;
    private static final int THIRD_DISTRICT_INDEX = 2;

    public District parse(String value) {
        String[] districts = value.split(DISTRICT_DELIMITER);
        if (districts.length == 1) {
            return new District(districts[FIRST_DISTRICT_INDEX]);
        } else if (districts.length == 2) {
            return new District(districts[FIRST_DISTRICT_INDEX], districts[SECOND_DISTRICT_INDEX]);
        }
        return new District(districts[FIRST_DISTRICT_INDEX],
                districts[SECOND_DISTRICT_INDEX], districts[THIRD_DISTRICT_INDEX]);
    }
}
