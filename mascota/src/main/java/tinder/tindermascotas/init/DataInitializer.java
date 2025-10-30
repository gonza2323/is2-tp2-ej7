package tinder.tindermascotas.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tinder.tindermascotas.entities.Pet;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.entities.Zone;
import tinder.tindermascotas.enums.Sexo;
import tinder.tindermascotas.enums.Type;
import tinder.tindermascotas.repositories.PetRepository;
import tinder.tindermascotas.repositories.UserRepository;
import tinder.tindermascotas.repositories.ZoneRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ZoneRepository zoneRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Override
    public void run(String... args) throws Exception {
        // Zonas
        Zone zone1 = Zone.builder().nombre("Capital").build();
        Zone zone2 = Zone.builder().nombre("Godoy Cruz").build();
        Zone zone3 = Zone.builder().nombre("Guaymallén").build();
        zoneRepository.save(zone1);
        zoneRepository.save(zone2);
        zoneRepository.save(zone3);

        // Usuarios
        User user1 = User.builder()
                .nombre("Lucia")
                .apellido("Alvarez")
                .mail("alvarezrossettilucia@gmail.com")
                .clave("1234")
                .zone(zone2)
                .providerUserId("102552769199584114079")
                .provider("google")
                .build();

        User user2 = User.builder()
                .nombre("Moni")
                .apellido("Argento")
                .mail("moniargento@gmail.com")
                .clave("1234")
                .zone(zone3)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // Mascotas
        Pet pet1 = Pet.builder()
                .nombre("Minnie")
                .sexo(Sexo.HEMBRA)
                .type(Type.GATO)
                .user(user1)
                .build();

        Pet pet2 = Pet.builder()
                .nombre("Pluto")
                .sexo(Sexo.MACHO)
                .type(Type.PERRO)
                .user(user1)
                .build();

        petRepository.save(pet1);
        petRepository.save(pet2);
    }
}
