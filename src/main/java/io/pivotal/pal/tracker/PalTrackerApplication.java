package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {


    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

//    @Bean
//    public TimeEntryRepository TimeEntryRepositoryBean() {
//        return new InMemoryTimeEntryRepository();
//    }

    @Bean
    public TimeEntryRepository TimeEntryRepositoryBean(DataSource datasource) {
        return new JdbcTimeEntryRepository(datasource);
    }
}