package com.github.siwonpawel.organizations.batch.organization;

import com.github.siwonpawel.organizations.batch.organization.dto.OrganizationDTO;
import com.github.siwonpawel.organizations.batch.organization.mapper.OrganizationMapper;
import com.github.siwonpawel.organizations.domain.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrganizationBatchImportJobConfig {

    public static final String FILENAME = "data/organizations.csv";
    private static final String[] CSV_COLUMNS = new String[]{
            "Name",
            "Website",
            "Country",
            "Description",
            "Founded",
            "Industry",
            "Number of employees"
    };
    private static final String DELIMITER = ",";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrganizationMapper organizationMapper;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    Job organizationsImportBatchJob(Step csvToDBImportStep) {
        return new JobBuilder("organizationsImportBatchJob")
                .repository(jobRepository)
                .start(csvToDBImportStep)
                .build();
    }

    @Bean
    Step csvToDBImportStep(
            ItemReader<OrganizationDTO> organizationDTOItemReader,
            ItemProcessor<OrganizationDTO, Organization> organizationDtoToOrganizationProcessor,
            ItemWriter<Organization> organizationToDBWriter
    ) {
        TaskletStep csvToDBImportStep = new StepBuilder("csvToDBImportStep")
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<OrganizationDTO, Organization>chunk(100)
                .reader(organizationDTOItemReader)
                .processor(organizationDtoToOrganizationProcessor)
                .writer(organizationToDBWriter)
                .build();

        log.info("Created step csvToDBImportStep: [{}]", csvToDBImportStep);

        return csvToDBImportStep;
    }

    @Bean
    ItemReader<OrganizationDTO> organizationDTOItemReader() {
        return new FlatFileItemReaderBuilder<OrganizationDTO>()
                .name("organizationDTOItemReader")
                .resource(new ClassPathResource(FILENAME))
                .targetType(OrganizationDTO.class)
                .delimited()
                .delimiter(DELIMITER)
                .names(CSV_COLUMNS)
                .linesToSkip(1)
                .build();
    }

    @Bean
    ItemProcessor<OrganizationDTO, Organization> organizationDTOItemProcessor() {
        return organizationMapper::mapOrganization;
    }

    @Bean
    ItemWriter<Organization> organizationToDbWriter() {
        return new JpaItemWriterBuilder<Organization>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
