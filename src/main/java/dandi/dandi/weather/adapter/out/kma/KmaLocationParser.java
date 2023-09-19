package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.advice.InternalServerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile({"local", "dev"})
public class KmaLocationParser implements InitializingBean {

    private static final String WEATHER_LOCATION_INSERT_QUERY = "INSERT INTO WEATHER_LOCATION (x, y) VALUES %s;";
    private static final int NX_INDEX = 5;
    private static final int NY_INDEX = 6;
    private static final String LOCATION_DELIMITER = ",";

    private final DataSource dataSource;

    public KmaLocationParser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = dataSource.getConnection()) {
            String insertQuery = generateWeatherLocationInsertQuery();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateWeatherLocationInsertQuery() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("src/main/resources/weather_location.csv"))) {
            return String.format(WEATHER_LOCATION_INSERT_QUERY, generateValuesQuery(br));
        } catch (IOException e) {
            throw new InternalServerException("기상청 위치 파일 데이터, 객체 변환 실패");
        }
    }

    private String generateValuesQuery(BufferedReader kmaLocationReader) throws IOException {
        String line;
        Set<KmaLocation> kmaLocations = new HashSet<>();
        while ((line = kmaLocationReader.readLine()) != null) {
            if (line.contains("격자 X")) {
                continue;
            }
            kmaLocations.add(parseToKmaLocation(line));
        }
        return kmaLocations.stream()
                .map(KmaLocation::toQueryValuesFormat)
                .collect(Collectors.joining(", "));
    }

    private KmaLocation parseToKmaLocation(String line) {
        String[] data = line.split(LOCATION_DELIMITER);
        int nx = Integer.parseInt(data[NX_INDEX]);
        int ny = Integer.parseInt(data[NY_INDEX]);
        return new KmaLocation(nx, ny);
    }

    static class KmaLocation {

        private static final String QUERY_VALUES_FORMAT = "(%d, %d)";

        private final int nx;
        private final int ny;

        public KmaLocation(int nx, int ny) {
            this.nx = nx;
            this.ny = ny;
        }

        public String toQueryValuesFormat() {
            return String.format(QUERY_VALUES_FORMAT, nx, ny);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KmaLocation that = (KmaLocation) o;
            return nx == that.nx && ny == that.ny;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nx, ny);
        }
    }
}
