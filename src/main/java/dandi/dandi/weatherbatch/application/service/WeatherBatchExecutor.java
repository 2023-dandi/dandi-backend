package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.batchcommons.application.port.out.BatchThreadSizePersistencePort;
import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.RemoteAdminMessageSender;
import dandi.dandi.weather.application.port.out.BaseDateTimeConvertor;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import dandi.dandi.weatherbatch.application.runner.WeatherBatch;
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
import java.util.Map;
import java.util.Objects;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Component
public class WeatherBatchExecutor {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");
    private static final long FIVE_SECONDS = 5000L;
    private static final String BATCH_NAME = "weatherBatch";

    private final ChunkSizePersistencePort chunkSizePersistencePort;
    private final BatchThreadSizePersistencePort batchThreadSizePersistencePort;
    private final JobLauncher jobLauncher;
    private final WeatherBatch weatherBatch;
    private final RemoteAdminMessageSender remoteAdminMessageSender;
    private final BaseDateTimeConvertor baseDateTimeConvertor;
    private final String batchAdminKey;

    public WeatherBatchExecutor(JobLauncher jobLauncher, ChunkSizePersistencePort chunkSizePersistencePort,
                                BatchThreadSizePersistencePort batchThreadSizePersistencePort, WeatherBatch weatherBatch,
                                RemoteAdminMessageSender remoteAdminMessageSender, BaseDateTimeConvertor baseDateTimeConvertor,
                                @Value("${spring.batch.admin-key}") String batchAdminKey) {
        this.jobLauncher = jobLauncher;
        this.chunkSizePersistencePort = chunkSizePersistencePort;
        this.batchThreadSizePersistencePort = batchThreadSizePersistencePort;
        this.weatherBatch = weatherBatch;
        this.remoteAdminMessageSender = remoteAdminMessageSender;
        this.baseDateTimeConvertor = baseDateTimeConvertor;
        this.batchAdminKey = batchAdminKey;
    }

    public void run(WeatherBatchRequest weatherBatchRequest) {
        long chunkSize = findChunkSizeIfRequestIsNull(weatherBatchRequest);
        long weatherApiThreadSize = findWeatherApiThreadSizeIfRequestIsNull(weatherBatchRequest);
        validateExecutionKey(weatherBatchRequest);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDateTime = baseDateTimeConvertor.convert(now);
        JobParameters jobParameters = new JobParameters(
                Map.of(
                        "baseDateTime", new JobParameter(baseDateTime.toString()),
                        "backOffPeriod", new JobParameter(FIVE_SECONDS),
                        "chunkSize", new JobParameter(chunkSize),
                        "weatherApiThreadSize", new JobParameter(weatherApiThreadSize)
                )
        );
        try {
            logger.info("{" + LocalDateTime.now() + "} WeatherBatch Start");
            JobExecution jobExecution = jobLauncher.run(weatherBatch.weatherBatch(), jobParameters);
            handleFailureIfFailed(baseDateTime.toString(), jobExecution.getExitStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | IOException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException | RuntimeException e) {
            String exceptionMessage = "Weather Batch Failed \r\n" + e.getMessage();
            remoteAdminMessageSender.sendMessage(now + exceptionMessage);
            logger.error(exceptionMessage);
        }
    }

    private int findChunkSizeIfRequestIsNull(WeatherBatchRequest weatherBatchRequest) {
        Integer chunkSize = weatherBatchRequest.getChunkSize();
        if (Objects.isNull(chunkSize)) {
            return chunkSizePersistencePort.findChunkSizeByName(BATCH_NAME);
        }
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Batch Size는 1이상이어야 합니다.");
        }
        return chunkSize;
    }

    private int findWeatherApiThreadSizeIfRequestIsNull(WeatherBatchRequest weatherBatchRequest) {
        Integer weatherApiThreadSize = weatherBatchRequest.getWeatherApiThreadSize();
        if (Objects.isNull(weatherApiThreadSize)) {
            return batchThreadSizePersistencePort.findBatchThreadSizeByName(BATCH_NAME);
        }
        if (weatherApiThreadSize < 1) {
            throw new IllegalArgumentException("날씨 API 스레드 풀 개수는 1 이상이어야 합니다.");
        }
        return weatherApiThreadSize;
    }

    private void validateExecutionKey(WeatherBatchRequest weatherBatchRequest) {
        if (!weatherBatchRequest.getBatchAdminKey().equals(batchAdminKey)) {
            throw new BatchException("날씨 Batch 실행 Key가 올바르지 않습니다.");
        }
    }

    private void handleFailureIfFailed(String now, ExitStatus exitStatus) {
        if (exitStatus.getExitCode().equals(COMPLETED.getExitCode())) {
            logger.info("{" + LocalDateTime.now() + "} WeatherBatch Complete");
            remoteAdminMessageSender.sendMessage(now + "Weather Batch Complete");
        }
        if (exitStatus.getExitCode().equals(FAILED.getExitCode())) {
            remoteAdminMessageSender.sendMessage(now + " Weather Batch Failed");
            logger.info("Weather Batch Failed \r\n {}", exitStatus.getExitDescription());
        }
    }
}
