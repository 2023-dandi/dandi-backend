package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchExecutorPort;
import dandi.dandi.weatherbatch.application.runner.WeatherBatch;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.weather.application.port.out.BaseTimeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Component
public class WeatherBatchExecutor implements WeatherBatchExecutorPort {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");
    private static final long FIVE_MINUTES = 5000000L;
    private static final long chuckSize = 1000L;

    private final JobLauncher jobLauncher;
    private final WeatherBatch weatherBatch;
    private final ErrorMessageSender errorMessageSender;
    private final BaseTimeConvertor baseTimeConvertor;
    private final String executionKey;

    public WeatherBatchExecutor(JobLauncher jobLauncher, WeatherBatch weatherBatch,
                                ErrorMessageSender errorMessageSender, BaseTimeConvertor baseTimeConvertor,
                                @Value("${weather.batch.key}") String executionKey) {
        this.jobLauncher = jobLauncher;
        this.weatherBatch = weatherBatch;
        this.errorMessageSender = errorMessageSender;
        this.baseTimeConvertor = baseTimeConvertor;
        this.executionKey = executionKey;
    }

    @Override
    public void runWeatherBatch(WeatherBatchRequest weatherBatchRequest) {
        validateExecutionKey(weatherBatchRequest);
        LocalDateTime now = LocalDateTime.now();
        LocalTime baseTime = baseTimeConvertor.convert(now.toLocalTime());
        LocalDateTime baseDateTime = LocalDateTime.of(now.toLocalDate(), baseTime);
        JobParameters jobParameters = new JobParameters(
                Map.of(
                        "baseDateTime", new JobParameter(baseDateTime.toString()),
                        "backOffPeriod", new JobParameter(FIVE_MINUTES),
                        "chunkSize", new JobParameter(chuckSize)
                )
        );
        try {
            logger.info("{" + LocalDateTime.now() + "} WeatherBatch Start");
            JobExecution jobExecution = jobLauncher.run(weatherBatch.weatherBatch(), jobParameters);
            handleFailureIfFailed(baseDateTime.toString(), jobExecution.getExitStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | IOException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException | RuntimeException e) {
            String exceptionMessage = "Weather Batch Failed \r\n" + e.getMessage();
            errorMessageSender.sendMessage(now + exceptionMessage);
            logger.error(exceptionMessage);
        }
    }

    private void validateExecutionKey(WeatherBatchRequest weatherBatchRequest) {
        if (!weatherBatchRequest.getKey().equals(executionKey)) {
            throw new BatchException("날씨 Batch 실행 Key가 올바르지 않습니다.");
        }
    }

    private void handleFailureIfFailed(String now, ExitStatus exitStatus) {
        if (exitStatus.getExitCode().equals(COMPLETED.getExitCode())) {
            logger.info("{" + LocalDateTime.now() + "} WeatherBatch Complete");
        }
        if (exitStatus.getExitCode().equals(FAILED.getExitCode())) {
            errorMessageSender.sendMessage(now + " Weather Batch Failed");
            logger.info("Weather Batch Failed \r\n {}", exitStatus.getExitDescription());
        }
    }
}
