package demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * This example is:
 * - self describing using HAL
 * -
 */
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ProductApplication.class)
                .listeners(new ApplicationPidFileWriter())
                .run(args);

    }


    // add in custom metrics and tap into lifecycle events
    @Component
    @RepositoryEventHandler
    public static class ProductMetricObserver {

        private Log log = LogFactory.getLog(getClass());
        private final CounterService counterService;

        @Autowired
        public ProductMetricObserver(CounterService counterService) {
            this.counterService = counterService;
        }

        @HandleAfterSave
        public void afterProductSaved(Product p) {
            this.log("saved", p);
        }

        @HandleAfterCreate
        public void afterProductCreate(Product p) {
            this.log("created", p);
        }

        @HandleAfterDelete
        public void afterProductDelete(Product p) {
            this.log("deleted", p);
        }

        private void log(String k, Product p) {
            log.info(String.format("product %s: %s", k, p.toString()));
            this.counterService.increment("products." + k);
        }
    }

    @Bean
    CommandLineRunner dummy(ProductRepository repository) {
        return args -> {
            repository.save(new Product("sku1", "description1", 10F));
            repository.save(new Product("sku2", "description2", 5F));
            repository.save(new Product("sku3", "description3", 11F));
        };
    }
}
