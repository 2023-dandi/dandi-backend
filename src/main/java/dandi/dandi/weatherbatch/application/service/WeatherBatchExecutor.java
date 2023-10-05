package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.RemoteAdminMessageSender;
import dandi.dandi.weather.application.port.out.BaseTimeConvertor;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchExecutorPort;
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
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Component
public class WeatherBatchExecutor implements WeatherBatchExecutorPort {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");
    private static final long FIVE_SECONDS = 5000L;
    private static final String BATCH_NAME = "weatherBatch";

    private final ChunkSizePersistencePort chunkSizePersistencePort;
    private final JobLauncher jobLauncher;
    private final WeatherBatch weatherBatch;
    private final RemoteAdminMessageSender remoteAdminMessageSender;
    private final BaseTimeConvertor baseTimeConvertor;
    private final String batchAdminKey;

    public WeatherBatchExecutor(ChunkSizePersistencePort chunkSizePersistencePort, JobLauncher jobLauncher,
                                WeatherBatch weatherBatch, RemoteAdminMessageSender remoteAdminMessageSender,
                                BaseTimeConvertor baseTimeConvertor,
                                @Value("${spring.batch.admin-key}") String batchAdminKey) {
        this.chunkSizePersistencePort = chunkSizePersistencePort;
        this.jobLauncher = jobLauncher;
        this.weatherBatch = weatherBatch;
        this.remoteAdminMessageSender = remoteAdminMessageSender;
        this.baseTimeConvertor = baseTimeConvertor;
        this.batchAdminKey = batchAdminKey;
    }

    @Override
    public void runWeatherBatch(WeatherBatchRequest weatherBatchRequest) {
        long chunkSize = findChunkSizeIfRequestIsNull(weatherBatchRequest);
        validateExecutionKey(weatherBatchRequest);
        LocalDateTime now = LocalDateTime.now();
        LocalTime baseTime = baseTimeConvertor.convert(now.toLocalTime());
        LocalDateTime baseDateTime = LocalDateTime.of(now.toLocalDate(), baseTime);
        JobParameters jobParameters = new JobParameters(
                Map.of(
                        "baseDateTime", new JobParameter(baseDateTime.toString()),
                        "backOffPeriod", new JobParameter(FIVE_SECONDS),
                        "chunkSize", new JobParameter(chunkSize)
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

    private void validateExecutionKey(WeatherBatchRequest weatherBatchRequest) {
        if (!weatherBatchRequest.getBatchAdminKey().equals(batchAdminKey)) {
            throw new BatchException("날씨 Batch 실행 Key가 올바르지 않습니다.");
        }
    }

    private void handleFailureIfFailed(String now, ExitStatus exitStatus) {
        if (exitStatus.getExitCode().equals(COMPLETED.getExitCode())) {
            logger.info("{" + LocalDateTime.now() + "} WeatherBatch Complete");
        }
        if (exitStatus.getExitCode().equals(FAILED.getExitCode())) {
            remoteAdminMessageSender.sendMessage(now + " Weather Batch Failed");
            logger.info("Weather Batch Failed \r\n {}", exitStatus.getExitDescription());
        }
    }
}
