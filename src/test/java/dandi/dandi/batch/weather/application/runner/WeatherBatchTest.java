package dandi.dandi.batch.weather.application.runner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dandi.dandi.weather.domain.Weather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import dandi.dandi.common.DatabaseCleaner;
import dandi.dandi.weather.adapter.out.persistence.jpa.WeatherJpaEntity;
import dandi.dandi.weather.adapter.out.persistence.jpa.WeatherLocationJpaEntity;
import dandi.dandi.weather.adapter.out.persistence.jpa.WeatherPersistenceAdapter;
import dandi.dandi.weather.adapter.out.persistence.jpa.WeatherRepository;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = {WeatherRepository.class})
@EntityScan(basePackageClasses = {WeatherJpaEntity.class, WeatherLocationJpaEntity.class})
@SpringBootTest(classes = {WeatherBatch.class, WeatherPersistenceAdapter.class, WeatherBatchTestConfig.class,
	DatabaseCleaner.class})
class WeatherBatchTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@AfterEach
	void clearDatabase() {
		databaseCleaner.truncate();
		databaseCleaner.initializeAutoIncrement();
	}

	@DisplayName("날씨 위치에 기반한 날씨 정보를 받아와 저장할 수 있다.")
	@Test
	void weatherBatchJob() throws Exception {
		long weatherLocationId = 1L;
		Weather firstWeather = new Weather.WeatherBuilder(LocalDateTime.now()).build();
		Weather secondWeather = new Weather.WeatherBuilder(LocalDateTime.now()).build();
		weatherRepository.saveAll(List.of(
				WeatherJpaEntity.ofWeather(firstWeather, weatherLocationId),
				WeatherJpaEntity.ofWeather(secondWeather, weatherLocationId)));
		LocalDateTime dateTime = LocalDateTime.of(2023, 9, 13, 15, 0);
		JobParameters jobParameters = new JobParameters(
			Map.of("baseDateTime", new JobParameter(dateTime.toString()),
					"backOffPeriod", new JobParameter(1L),
					"chunkSize", new JobParameter(10L)));
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(new WeatherLocationJpaEntity(110, 100));
		entityManager.getTransaction().commit();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertAll(
			() -> assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED),
			() -> assertThat(weatherRepository.findAll()).hasSize(24)
		);
	}

	@DisplayName("날씨 위치에 기반한 날씨 정보를 받아오는 도중 WeatherRequestRetryableException이 발생하면 재시도 한다.")
	@Test
	void weatherBatchJob_RetryableException_Success() throws Exception {
		LocalDateTime dateTime = LocalDateTime.of(2021, 9, 13, 15, 0);
		JobParameters jobParameters = new JobParameters(
				Map.of("baseDateTime", new JobParameter(dateTime.toString()),
						"backOffPeriod", new JobParameter(1L),
						"chunkSize", new JobParameter(10L)));
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(new WeatherLocationJpaEntity(110, 100));
		entityManager.getTransaction().commit();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertAll(
			() -> assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED),
			() -> assertThat(weatherRepository.findAll()).hasSize(24)
		);
	}

	@DisplayName("날씨 위치에 기반한 날씨 정보를 받아오는 도중 WeatherRequestRetryableException이 발생하고 1회의 재시도도 실패하면 재시도 하지 않는다.")
	@Test
	void weatherBatchJob_RetryableException_Failed() throws Exception {
		LocalDateTime dateTime = LocalDateTime.of(2022, 9, 13, 15, 0);
		JobParameters jobParameters = new JobParameters(
				Map.of("baseDateTime", new JobParameter(dateTime.toString()),
						"backOffPeriod", new JobParameter(1L),
						"chunkSize", new JobParameter(10L)));
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(new WeatherLocationJpaEntity(110, 100));
		entityManager.getTransaction().commit();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertAll(
			() -> assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.getExitCode()),
			() -> assertThat(weatherRepository.findAll()).hasSize(0)
		);
	}

	@DisplayName("날씨 위치에 기반한 날씨 정보를 받아오는 도중 WeatherRequestFatalException이 발생하고 실패한다.")
	@Test
	void weatherBatchJob_FatalException_Failed() throws Exception {
		LocalDateTime dateTime = LocalDateTime.of(2020, 9, 13, 15, 0);
		JobParameters jobParameters = new JobParameters(
				Map.of("baseDateTime", new JobParameter(dateTime.toString()),
						"backOffPeriod", new JobParameter(1L),
						"chunkSize", new JobParameter(10L)));
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(new WeatherLocationJpaEntity(110, 100));
		entityManager.getTransaction().commit();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertAll(
			() -> assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.getExitCode()),
			() -> assertThat(weatherRepository.findAll()).hasSize(0)
		);
	}
}
