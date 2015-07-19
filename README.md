# the Operationalized Application

So your application is code-complete, but is it ready for production? In this talk, we'll look at all the things that Spring Boot provides to integrate your application more readily with production, or operational, concerns.

We'll look at:

* recording and reporting of application state (like `/trace`, and `/health`) through the Spring Boot Actuator framework
* using the Spring Boot Actuator metrics support to generate and expose custom metrics  
* how to build joined-up views of those metrics thanks to tools like Graphite and OpenTSDB
* logging and the `/logfile` endpoint?
* 12-factor app style configuration
* building APIs that are easy to understand with HAL and the HAL Browser
* integrating security concerns like HTTP(S).
* application deployment: executable `.jar`s, PID files,
* how to get scale for things like HTTP session state using Spring Session.
* optimizing for continuous delivery by exporting the Git commit ID in the `/info` endpoint, exposing something like SolarMetrics
* database migrations with `/flyway` and `/liquibase`
* succinctly describing deployment metadata using a manifest.yml or a Dockerfile


# Using OpenTSDB to see the metrics

This example reports metrics to OpenTSDB. It expects to find OpenTSDB running on port 4242, on your localhost.
I've included a Docker compose image (that I in turn borrowed from the [Spring Boot
project](http://github.com/spring-projects/spring-boot/) samples.  To run it, use Docker compose:

```sh
docker-compose up
```

