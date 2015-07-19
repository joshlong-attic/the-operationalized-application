package demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.opentsdb.DefaultOpenTsdbNamingStrategy;
import org.springframework.boot.actuate.metrics.opentsdb.OpenTsdbMetricWriter;
import org.springframework.boot.actuate.metrics.opentsdb.OpenTsdbNamingStrategy;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;


@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductApplication.class)
                .listeners(new ApplicationPidFileWriter())
                .run(args);
    }

    public static class ApplicationSickEvent extends ApplicationEvent {

        private Exception exception;

        public Exception getException() {
            return exception;
        }

        public ApplicationSickEvent(Exception e) {
            super(e);
            this.exception = e;
        }
    }

    @Component("productHealthIndicator")
    public static class ProductHealthIndicator implements HealthIndicator, ApplicationListener<ApplicationSickEvent> {

        private volatile Exception exception;

        @Override
        public void onApplicationEvent(ApplicationSickEvent applicationSickEvent) {
            this.exception = applicationSickEvent.getException();
        }

        @Override
        public Health health() {
            return (null != this.exception ? Health.down(this.exception) : Health.up()).build();
        }
    }

    @Bean
    @ExportMetricWriter
    @ConfigurationProperties("metrics.export")
    OpenTsdbMetricWriter openTsdbMetricWriter() {
        OpenTsdbMetricWriter writer = new OpenTsdbMetricWriter();
        writer.setNamingStrategy(namingStrategy());
        return writer;
    }

    @Bean
    @ConfigurationProperties("metrics.names")
    OpenTsdbNamingStrategy namingStrategy() {
        return new DefaultOpenTsdbNamingStrategy();
    }

    // add in custom metrics and tap into lifecycle events
    @Component
    @RepositoryEventHandler
    public static class ProductMetricsObserver {

        private final Log log = LogFactory.getLog(getClass());
        private final GaugeService gaugeService;
        private final OpenTsdbMetricWriter metricWriter;

        @Autowired
        public ProductMetricsObserver(GaugeService gaugeService,
                                      OpenTsdbMetricWriter metricWriter) {
            this.gaugeService = gaugeService;
            this.metricWriter = metricWriter;
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
            this.log.info(String.format("product %s: %s", k, p.toString()));
            this.gaugeService.submit("products." + k, System.currentTimeMillis());
            this.metricWriter.flush();
        }
    }

    @RestController
    public static class MessageRestController {

        private final CounterService counterService;

        private final ApplicationEventPublisher applicationEventPublisher;

        @Autowired
        public MessageRestController(CounterService counterService, ApplicationEventPublisher publisher) {
            this.counterService = counterService;
            this.applicationEventPublisher = publisher;
        }

        @RequestMapping("/healthy")
        @ResponseStatus(code = HttpStatus.ACCEPTED)
        void healthy() {
        }

        @RequestMapping("/sick")
        @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
        void sick() {
            this.applicationEventPublisher.publishEvent(
                    new ApplicationSickEvent(new RuntimeException("I'm not a teapot!")));
        }

        @RequestMapping("/message")
        String message() {
            this.counterService.increment("message.viewed");
            return "Hello, world";
        }
    }

    @Bean
    CommandLineRunner dummy(ProductRepository repository) {
        return args ->
            IntStream.range(0, 100).forEach(x -> repository.save(new Product("sku" + x, "description" + x, 10f)));
    }
}