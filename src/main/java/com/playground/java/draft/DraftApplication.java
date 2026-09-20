package com.playground.java.draft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DraftApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DraftApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DraftApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.atInfo()
           .setMessage("Playground service started successfully")
           .addKeyValue("environment", "local")
           .addKeyValue("argumentsCount", args.getSourceArgs() != null ? args.getSourceArgs().length : 0)
           .log();
    }
}
