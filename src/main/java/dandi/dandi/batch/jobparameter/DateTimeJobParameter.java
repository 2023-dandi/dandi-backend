package dandi.dandi.batch.jobparameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeJobParameter {

    private final LocalDateTime localDateTime;

    public DateTimeJobParameter(String localDateTime) {
        this.localDateTime = LocalDateTime.parse(localDateTime);
    }

    public LocalDate ofMinusDays(long days) {
        return localDateTime.minusDays(days).toLocalDate();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public String toString() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
