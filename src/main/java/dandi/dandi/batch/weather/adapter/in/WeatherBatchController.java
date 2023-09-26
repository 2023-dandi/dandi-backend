package dandi.dandi.batch.weather.adapter.in;

import dandi.dandi.batch.weather.application.port.in.WeatherBatchExecutorPort;
import dandi.dandi.batch.weather.application.service.WeatherBatchExecutor;
import dandi.dandi.batch.weather.application.port.in.WeatherBatchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherBatchController {

    private final WeatherBatchExecutorPort weatherBatchExecutorPort;

    public WeatherBatchController(WeatherBatchExecutorPort weatherBatchExecutorPort) {
        this.weatherBatchExecutorPort = weatherBatchExecutorPort;
    }

    @PostMapping("/batch/weather")
    public ResponseEntity<Void> runWeatherBatch(@RequestBody WeatherBatchRequest weatherBatchRequest) {
        weatherBatchExecutorPort.runWeatherBatch(weatherBatchRequest);
        return ResponseEntity.noContent().build();
    }
}
