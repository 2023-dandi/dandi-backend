package dandi.dandi.batch.weather;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.batch.image.UnusedImageBatchScheduler;
import dandi.dandi.batch.image.UnusedImageDeletionBatch;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
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
class WeatherBatchSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private ErrorMessageSender errorMessageSender;
    @Mock
    private WeatherBatch weatherBatch;
    @InjectMocks
    private WeatherBatchScheduler weatherBatchScheduler;

    @DisplayName("날씨 Batch 작업 중 Batch 관련 예외가 발생하면 관리자에게 메시지를 전송한다.")
    @Test
    void runWeatherBatch_BatchException() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        when(jobLauncher.run(any(), any()))
                .thenThrow(BatchException.class);

        weatherBatchScheduler.runWeatherBatch();

        verify(errorMessageSender).sendMessage(anyString());
    }

    @DisplayName("날씨 Batch가 실패로 종료되면 관리자에게 메시지를 전송한다.")
    @Test
    void runWeatherBatch_FailedExitStatus() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        when(jobLauncher.run(any(), any()))
                .thenReturn(jobExecution);
        when(jobExecution.getExitStatus())
                .thenReturn(ExitStatus.FAILED);

        weatherBatchScheduler.runWeatherBatch();

        verify(errorMessageSender).sendMessage(anyString());
    }
}
