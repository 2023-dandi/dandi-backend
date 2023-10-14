package dandi.dandi.weatherbatch.application.service;

import dandi.dandi.batchcommons.application.port.out.BatchThreadSizePersistencePort;
import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.batchcommons.exception.BatchException;
import dandi.dandi.errormessage.application.port.out.RemoteAdminMessageSender;
import dandi.dandi.weather.application.port.out.BaseTimeConvertor;
import dandi.dandi.weatherbatch.application.port.in.WeatherBatchRequest;
import dandi.dandi.weatherbatch.application.runner.WeatherBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherBatchExecutorTest {

    private static final String JOB_NAME = "weatherBatch";

    private final ChunkSizePersistencePort chunkSizePersistencePort = Mockito.mock(ChunkSizePersistencePort.class);
    private final BatchThreadSizePersistencePort batchThreadSizePersistencePort =
            Mockito.mock(BatchThreadSizePersistencePort.class);
    private final JobLauncher jobLauncher = Mockito.mock(JobLauncher.class);
    private final WeatherBatch weatherBatch = Mockito.mock(WeatherBatch.class);
    private final RemoteAdminMessageSender remoteAdminMessageSender = Mockito.mock(RemoteAdminMessageSender.class);
    private final BaseTimeConvertor baseTimeConvertor = Mockito.mock(BaseTimeConvertor.class);
    private final String batchAdminKey = "key";
    private final WeatherBatchExecutor weatherBatchExecutor = new WeatherBatchExecutor(
            jobLauncher, chunkSizePersistencePort, batchThreadSizePersistencePort,
            weatherBatch, remoteAdminMessageSender, baseTimeConvertor, batchAdminKey);

    @BeforeEach
    void setUp() {
        when(chunkSizePersistencePort.findChunkSizeByName(JOB_NAME))
                .thenReturn(100);
    }

    @DisplayName("WeatherBatchRequest의 chunkSize가 null이라면 DB에서 chunkSize를 조회한다.")
    @Test
    void runWeatherBatch_FindChunkSizeInDBIfRequestChunkSizeIsNull() {
        when(baseTimeConvertor.convert(any()))
                .thenReturn(LocalTime.of(2, 0));
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, null, 10);

        weatherBatchExecutor.run(weatherBatchRequest);

        verify(chunkSizePersistencePort).findChunkSizeByName(JOB_NAME);
    }

    @DisplayName("WeatherBatchRequest의 chunkSize가 1보다 작다면 예외를 발생시킨다.")
    @Test
    void runWeatherBatch_InvalidChunkSizeException() {
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, 0, 10);

        assertThatThrownBy(() -> weatherBatchExecutor.run(weatherBatchRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("WeatherBatchRequest의 batchThreadSize가 null이라면 DB에서 batchThreadSize를 조회한다.")
    @Test
    void runWeatherBatch_FindBatchThreadSizeInDBIfRequestBatchThreadSizeIsNull() {
        when(baseTimeConvertor.convert(any()))
                .thenReturn(LocalTime.of(2, 0));
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, 10, null);

        weatherBatchExecutor.run(weatherBatchRequest);

        verify(batchThreadSizePersistencePort).findBatchThreadSizeByName(JOB_NAME);
    }

    @DisplayName("WeatherBatchRequest의 batchThreadSize가 1보다 작다면 예외를 발생시킨다.")
    @Test
    void runWeatherBatch_InvalidBatchThreadSizeException() {
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, 10, 0);

        assertThatThrownBy(() -> weatherBatchExecutor.run(weatherBatchRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("올바르지 않은 Batch key로 날씨 Batch 작업을 실행하려 하면 예외를 발생시킨다.")
    @Test
    void runWeatherBatch_InvalidExecutionKey() {
        String invalidExecutionKey = "invalidExecutionKey";
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(invalidExecutionKey, 100, 10);

        assertThatThrownBy(() -> weatherBatchExecutor.run(weatherBatchRequest))
                .isInstanceOf(BatchException.class)
                .hasMessage("날씨 Batch 실행 Key가 올바르지 않습니다.");
    }

    @DisplayName("날씨 Batch 작업 중 Batch 관련 예외가 발생하면 관리자에게 메시지를 전송한다.")
    @Test
    void runWeatherBatch_BatchException() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        when(jobLauncher.run(any(), any()))
                .thenThrow(BatchException.class);
        when(baseTimeConvertor.convert(any()))
                .thenReturn(LocalTime.of(2, 0));
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, 100, 10);

        weatherBatchExecutor.run(weatherBatchRequest);

        verify(remoteAdminMessageSender).sendMessage(anyString());
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
        when(baseTimeConvertor.convert(any()))
                .thenReturn(LocalTime.of(2, 0));
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(batchAdminKey, 100, 10);

        weatherBatchExecutor.run(weatherBatchRequest);

        verify(remoteAdminMessageSender).sendMessage(anyString());
    }
}
