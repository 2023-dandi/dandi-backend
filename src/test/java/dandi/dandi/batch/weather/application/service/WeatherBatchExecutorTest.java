package dandi.dandi.batch.weather.application.service;

import dandi.dandi.batch.exception.BatchException;
import dandi.dandi.batch.weather.application.port.in.WeatherBatchRequest;
import dandi.dandi.batch.weather.application.runner.WeatherBatch;
import dandi.dandi.errormessage.application.port.out.ErrorMessageSender;
import dandi.dandi.weather.application.port.out.BaseTimeConvertor;
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

    private final JobLauncher jobLauncher = Mockito.mock(JobLauncher.class);
    private final WeatherBatch weatherBatch = Mockito.mock(WeatherBatch.class);
    private final ErrorMessageSender errorMessageSender = Mockito.mock(ErrorMessageSender.class);
    private final BaseTimeConvertor baseTimeConvertor = Mockito.mock(BaseTimeConvertor.class);
    private final String weatherBatchExecutionKey = "key";
    private final WeatherBatchExecutor weatherBatchExecutor = new WeatherBatchExecutor(
            jobLauncher, weatherBatch, errorMessageSender, baseTimeConvertor, weatherBatchExecutionKey);

    @DisplayName("올바르지 않은 Batch key로 날씨 Batch 작업을 실행하려 하면 예외를 발생시킨다.")
    @Test
    void runWeatherBatch_InvalidExecutionKey() {
        String invalidExecutionKey = "invalidExecutionKey";
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(invalidExecutionKey);

        assertThatThrownBy(() -> weatherBatchExecutor.runWeatherBatch(weatherBatchRequest))
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
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(weatherBatchExecutionKey);

        weatherBatchExecutor.runWeatherBatch(weatherBatchRequest);

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
        when(baseTimeConvertor.convert(any()))
                .thenReturn(LocalTime.of(2, 0));
        WeatherBatchRequest weatherBatchRequest = new WeatherBatchRequest(weatherBatchExecutionKey);

        weatherBatchExecutor.runWeatherBatch(weatherBatchRequest);

        verify(errorMessageSender).sendMessage(anyString());
    }
}