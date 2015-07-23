package demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {

    @Bean
    HealthIndicator healthIndicator() {
        return () -> Health.status("I <3 Uberconf").build();
    }

    @Bean
    CommandLineRunner dummy(ProductRepository pr) {
        return args ->
                IntStream.range(0, 1000).forEach(
                        i -> pr.save(new Product("sku" + i))
                );
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

