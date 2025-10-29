package ar.edu.uncuyo.gimnasio_sport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GimnasioSportApplication {

    public static void main(String[] args) {
        SpringApplication.run(GimnasioSportApplication.class, args);
    }

}
