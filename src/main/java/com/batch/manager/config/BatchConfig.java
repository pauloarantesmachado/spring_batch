package com.batch.manager.config;

import com.batch.manager.entity.deviceTracker.DeviceDto;
import com.batch.manager.entity.deviceTracker.DeviceTracker;
import com.batch.manager.repository.DeviceTrackerRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@AllArgsConstructor
public class BatchConfig {


    @Autowired
    private DeviceTrackerRepository deviceTrackerRepository;

    @Bean
    public FlatFileItemReader<DeviceDto> itemReader() {
        FlatFileItemReader<DeviceDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/Fatec_Locations.csv"));
        flatFileItemReader.setName("csvReader");
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<DeviceDto> lineMapper() {

        DefaultLineMapper<DeviceDto> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("idText", "registrationDate", "latitude", "longitude", "name", "code", "location");
        BeanWrapperFieldSetMapper<DeviceDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(DeviceDto.class);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public ItemDeviceTrackerProcessor processor() {
        return new ItemDeviceTrackerProcessor();
    }

    @Bean
    public RepositoryItemWriter<DeviceTracker> writer() {
        RepositoryItemWriter<DeviceTracker> writer = new RepositoryItemWriter<>();
        writer.setRepository(deviceTrackerRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
        public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, RepositoryItemWriter<DeviceTracker> writer) {
            return new StepBuilder("step1", jobRepository)
                    .<DeviceDto, DeviceTracker> chunk(10, transactionManager)
                    .reader(itemReader())
                    .processor(processor())
                    .writer(writer)
                    .taskExecutor(taskExecutor())
                    .build();
    }

    @Bean
    public Job importCustomerJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importCustomer", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}
