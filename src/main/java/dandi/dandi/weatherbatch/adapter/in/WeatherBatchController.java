package dandi.dandi.weatherbatch.adapter.in;

import dandi.dandi.weatherbatch.application.port.in.WeatherBatchExecutionPort;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherBatchController {

    private final WeatherBatchExecutionPort weatherBatchExecutionPort;

    public WeatherBatchController(WeatherBatchExecutionPort weatherBatchExecutionPort) {
        this.weatherBatchExecutionPort = weatherBatchExecutionPort;
    }

    @PostMapping("/batch/weather")
    public ResponseEntity<Void> runWeatherBatch(@RequestBody WeatherBatchRequest weatherBatchRequest) {
        weatherBatchExecutionPort.runWeatherBatch(weatherBatchRequest);
        return ResponseEntity.noContent().build();
    }
}
