package dandi.dandi.unusedimagebatch.application.service;

import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.RemoteAdminMessageSender;
import dandi.dandi.unusedimagebatch.application.runner.UnusedImageDeletionBatch;
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
    private static final String BATCH_NAME = "unusedImageDeletion";

    private final ChunkSizePersistencePort chunkSizePersistencePort;
    private final JobLauncher jobLauncher;
    private final UnusedImageDeletionBatch unusedImageDeletionBatch;
    private final RemoteAdminMessageSender remoteAdminMessageSender;

    public UnusedImageBatchScheduler(ChunkSizePersistencePort chunkSizePersistencePort, JobLauncher jobLauncher,
                                     UnusedImageDeletionBatch unusedImageDeletionBatch,
                                     RemoteAdminMessageSender remoteAdminMessageSender) {
        this.chunkSizePersistencePort = chunkSizePersistencePort;
        this.jobLauncher = jobLauncher;
        this.unusedImageDeletionBatch = unusedImageDeletionBatch;
        this.remoteAdminMessageSender = remoteAdminMessageSender;
    }

    @Scheduled(cron = "0 0 4 * * SUN")
    public void runUnusedImageDeletionBatch() {
        long chunkSize = chunkSizePersistencePort.findChunkSizeByName(BATCH_NAME);
        String today = LocalDateTime.now().toString();
        JobParameters jobParameters = new JobParameters(
                Map.of(
                        "dateTime", new JobParameter(today),
                        "chunkSize", new JobParameter(chunkSize)
                )
        );
        try {
            jobLauncher.run(unusedImageDeletionBatch.deleteUnusedImageJob(), jobParameters);
        } catch (BatchException | JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            remoteAdminMessageSender.sendMessage(today + " Unused Image Deletion Batch failed");
            logger.info("Unused Image Deletion Batch failed \r\n {}", e.getMessage());
        }
    }
}
