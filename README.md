# the Operationalized Application

So your application is code-complete, but is it ready for production? In this talk, we'll look at all the things that Spring Boot provides to integrate your application more readily with production, or operational, concerns.

## For Sure

* recording and reporting of application state (like `/trace`, and `/health`) through the Spring Boot Actuator framework
* using the Spring Boot Actuator metrics support to generate and expose custom metrics  
* how to build joined-up views of those metrics thanks to tools like `jconsole`, OpenTSDB
* succinctly describing deployment metadata using a `manifest.yml` or a `Dockerfile`
* 12-factor app style configuration w/ the Spring Boot primatives, or with the config server.
* building APIs that are easy to understand with HAL and the HAL Browser
* optimizing for continuous delivery by exporting the Git commit ID in the `/info` endpoint, exposing something like SolarMetrics
* integrating security concerns like HTTP(S).
* application deployment: executable `.jar`s, PID files,
* logging
* SonarQube integration? http://www.sonarqube.org/ http://blogs.sourceallies.com/2012/03/code-quality-metrics-with-sonar-part-i/

## Possibly
* database migrations with `/flyway` and `/liquibase`

# Using OpenTSDB to see the metrics
This example borrows from the sample in the Spring Boot codebase' samples directory.
I am using Docker to stand up both OpenTSDB and the dashboard from Ticketmaster, Metrilyx.

To get everything up and running, check out `opentsdb.sh` in the root of the directory.
If you're not using `boot2docker` on OSX then comment out the second line and uncomment
the third line of the shell script and then run it. [Here are the detailed instructions](https://registry.hub.docker.com/u/dreampuf/metrilyx/).

You should be able to then point your browser to both `http://$BOOT2DOCkER_IP:4242/` for
OpenTSDB and to `http://$BOOT2DOCKER_IP:8081` for the Metrilyx visualization.

# SonarQube

There's a script, `sonarqube.sh`, that can generate all sorts
of _very_ cool information about the state of the application. Run it and then visit `http://$BOOT2DOCKER_IP:9000/`
and drill down to the dashboards for your application. All of this information could be
pulled together into a very clean CI pipleline.

