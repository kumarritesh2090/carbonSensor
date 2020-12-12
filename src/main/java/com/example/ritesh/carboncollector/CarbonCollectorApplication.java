package com.example.ritesh.carboncollector;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableKafka
@EnableMongoRepositories
@Log4j
//@EnableSwagger2
@EnableTransactionManagement
public class CarbonCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonCollectorApplication.class, args);
    }

  /*  @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }
*/

}
