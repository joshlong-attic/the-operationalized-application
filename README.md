# the Operationalized Application

So your application is code-complete, but is it ready for production? In this talk, we'll look at all the things that Spring Boot provides to integrate your application more readily with production, or operational, concerns.

We'll look at:


* recording and reporting of application state (like `/trace`, and `/health`) through the Spring Boot Actuator framework
* using the Spring Boot Actuator metrics support to generate and expose custom metrics  
* how to build joined-up views of those metrics thanks to tools like Graphite and OpenTSDB
* logging
* building APIs that are easy to understand with HAL and the HAL Browser
* integrating security concerns like HTTP(S).
* application deployment: executable `.jar`s, PID files,
* how to get scale for things like HTTP session state using Spring Session.
* optimizing for continuous delivery by exporting the Git commit ID in the `/info` endpoint, exposing something like SolarMetrics

-- reporting of the app state (actuator features including custom metrics)
-- database migrations: including the actuator endpoint /flyway and /liquibase
-- security (look at how to integrate real-world security and how Spring Security handles it out-of-the-box)
-- are your APIs self describing? (HAL & HAL-browser)
-- are your APIs using HTTPS or something?
-- can i run my application as a service (1.30.m2 has executable jars!)
-- where requured, how do i handle clustering ? one use case for this is in the occasion web sessin. Spring Session.
-- succinctly describing deployment metadata using a manifest.yml or a Dockerfile
-- is the git commit id and service ID available somewhere for continuous delivery somewere?
-- logging! also check out the /logfile endpoint data.
-- is there a dashboard for understanding the various dimensions of the code?
-- how can i handle configuration w/ - eg - passwords and so on (spring cloud config server)
-- does the app support 12f app configuration of things lke DB resources?
-- PID event listener
