package ar.edu.uncuyo.ej2b.init;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfig {

    private static final long SEED = 12345L; // use any fixed seed for reproducible results

    @Bean
    public Random seededRandom() {
        return new Random(SEED);
    }
}
