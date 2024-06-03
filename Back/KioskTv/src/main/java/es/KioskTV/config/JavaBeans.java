package es.KioskTV.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import es.KioskTV.Mapper.Mapper;

/**
 * Configuration class for defining Java beans.
 */
@Configuration
public class JavaBeans {

    /**
     * Defines a bean for Mapper.
     *
     * @return an instance of Mapper
     */
    @Bean
    public Mapper mapper() {
        return new Mapper();
    }

}
