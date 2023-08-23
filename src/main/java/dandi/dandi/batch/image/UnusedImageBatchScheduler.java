package dandi.dandi.batch.image;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class UnusedImageBatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger("asyncLogger");

    private final JobLauncher jobLauncher;
    private final UnusedImageDeletionBatch unusedImageDeletionBatch;
    private final ErrorMessageSender errorMessageSender;

    public UnusedImageBatchScheduler(JobLauncher jobLauncher, UnusedImageDeletionBatch unusedImageDeletionBatch, ErrorMessageSender errorMessageSender) {
        this.jobLauncher = jobLauncher;
        this.unusedImageDeletionBatch = unusedImageDeletionBatch;
        this.errorMessageSender = errorMessageSender;
    }

    @Scheduled(cron = "0 0 4 * * SUN")
    public void runUnusedImageDeletionBatch() {
        String today = LocalDateTime.now().toString();
        JobParameter dateJobParameter = new JobParameter(today);
        JobParameters jobParameters = new JobParameters(Map.of("dateTime", dateJobParameter));
        try {
            jobLauncher.run(unusedImageDeletionBatch.deleteUnusedImageJob(), jobParameters);
        } catch (BatchException | JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            errorMessageSender.sendMessage(today + " Unused Image Deletion Batch failed");
            logger.info("Unused Image Deletion Batch failed \r\n {}", e.getMessage());
        }
    }
}
