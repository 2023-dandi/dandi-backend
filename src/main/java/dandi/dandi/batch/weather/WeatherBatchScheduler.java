package dandi.dandi.batch.weather;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.batch.image.UnusedImageDeletionBatch;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.batch.core.ExitStatus.FAILED;


@Component
public class WeatherBatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");
    private static final long FIVE_MINUTES = 5000000L;

    private final JobLauncher jobLauncher;
    private final WeatherBatch weatherBatch;
    private final ErrorMessageSender errorMessageSender;

    public WeatherBatchScheduler(JobLauncher jobLauncher, WeatherBatch weatherBatch, ErrorMessageSender errorMessageSender) {
        this.jobLauncher = jobLauncher;
        this.weatherBatch = weatherBatch;
        this.errorMessageSender = errorMessageSender;
    }

    @Scheduled(cron = "0 10 2/3 * * *")
    public void runWeatherBatch() {
        String now = LocalDateTime.now().toString();
        JobParameters jobParameters = new JobParameters(
                Map.of(
                        "dateTime", new JobParameter(now),
                        "backOffPeriod", new JobParameter(FIVE_MINUTES)
                )
        );
        try {
            JobExecution jobExecution = jobLauncher.run(weatherBatch.weatherBatch(), jobParameters);
            handleFailureIfFailed(now, jobExecution.getExitStatus());
        } catch (BatchException | JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            errorMessageSender.sendMessage(now + " Weather Batch Failed");
            logger.info("Weather Batch Failed \r\n {}", e.getMessage());
        }
    }

    private void handleFailureIfFailed(String now, ExitStatus exitStatus) {
        if (exitStatus.getExitCode().equals(FAILED.getExitCode())) {
            errorMessageSender.sendMessage(now + " Weather Batch Failed");
            logger.info("Weather Batch Failed \r\n {}", exitStatus.getExitDescription());
        }
    }
}
