package ar.edu.itba.cep.executor_service.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class.
 */
@Configuration
@ComponentScan(value = {
        "ar.edu.itba.cep.executor_service.*.config"
})
public class AppConfig {
}
