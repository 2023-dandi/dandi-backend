package dandi.dandi.unusedimagebatch.application.service;

import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.unusedimagebatch.application.runner.UnusedImageDeletionBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnusedImageBatchSchedulerTest {

    @Mock
    private ChunkSizePersistencePort chunkSizePersistencePort;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private ErrorMessageSender errorMessageSender;
    @Mock
    private UnusedImageDeletionBatch unusedImageDeletionBatch;
    @InjectMocks
    private UnusedImageBatchScheduler unusedImageBatchScheduler;

    @Test
    @DisplayName("미사용 이미지 삭제 Batch에 실패하면 관리자에게 메시지를 전송한다.")
    void runUnusedImageDeletionBatch_Failure() throws JobInstanceAlreadyCompleteException, JobRestartException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException {
        when(chunkSizePersistencePort.findChunkSizeByName("unusedImageDeletion"))
                .thenReturn(100);
        when(jobLauncher.run(any(), any()))
                .thenThrow(BatchException.class);

        unusedImageBatchScheduler.runUnusedImageDeletionBatch();

        verify(errorMessageSender).sendMessage(anyString());
    }
}