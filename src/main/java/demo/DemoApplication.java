package demo;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.statsd.StatsdMetricWriter;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {


    private static Logger LOGGER = LoggerFactory.getLogger(
            DemoApplication.class);


    @Bean
    HealthIndicator healthIndicator() {
        return () -> Health.status("I <3 Spring!").build();
    }

    @Bean
    GraphiteReporter graphiteReporter(MetricRegistry registry,
                                      @Value("${graphite.host}") String host,
                                      @Value("${graphite.port}") int port) {

        GraphiteReporter reporter = GraphiteReporter
                .forRegistry(registry)
                .prefixedWith("products")
                .build(new Graphite(host, port));

        reporter.start(2, TimeUnit.SECONDS);

        return reporter;
    }

    @Bean
    @ExportMetricWriter
    StatsdMetricWriter statsdMetricWriter(
            @Value("${statsd.host}") String host,
            @Value("${statsd.port}") int port) {
        return new StatsdMetricWriter("statsd-products", host, port);
    }


    @Bean
    CommandLineRunner dummy(ProductRepository pr) {
        return args ->
                IntStream.range(0, 1000)
                        .forEach(i -> pr.save(new Product("sku" + i)));
    }

    @Component
    @RepositoryEventHandler
    public static class ProductEventHandler {

        @Autowired
        private CounterService counterService;


        @HandleAfterCreate
        public void create(Product p) {
            count("products.create", p);
        }

        @HandleAfterSave
        public void save(Product p) {
            count("products.save", p);
            count("products." + p.getId() + ".save", p);
        }

        @HandleAfterDelete
        public void delete(Product p) {
            count("products.delete", p);
        }

        private void count(String evt, Product p) {
            LogstashMarker logstashMarker = Markers.append("event", evt)
                    .and(Markers.append("sku", p.getSku()))
                    .and(Markers.append("id", p.getId()));

            LOGGER.info(logstashMarker, evt);

            this.counterService.increment(evt);
            this.counterService.increment("meter." + evt);
        }
    }

    public static void main(String[] args) {
        //SpringApplication.run(DemoApplication.class, args);
        new SpringApplicationBuilder(DemoApplication.class)
                .listeners(new ApplicationPidFileWriter())
                .run(args);
    }
}

