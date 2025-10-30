package ar.edu.uncuyo.ej2b.repository;

import ar.edu.uncuyo.ej2b.entity.Localidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LocalidadRepositoryTest {

    @Autowired
    private LocalidadRepository localidadRepository;

    @Nested
    class ExistsByDenominacionAndEliminadoFalseTests {
        @Test
        void returnsTrueWhenSameNameAndNotEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndEliminadoFalse("Localidad");
            assertThat(result).isTrue();
        }

        @Test
        void returnsFalseWhenSameNameAndEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(true).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndEliminadoFalse("Localidad");
            assertThat(result).isFalse();
        }

        @Test
        void returnsFalseWhenDifferentName() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndEliminadoFalse("Other");
            assertThat(result).isFalse();
        }
    }

    @Nested
    class ExistsByDenominacionAndIdNotAndEliminadoFalseTests {
        @Test
        void returnsTrueWhenSameNameAndDifferentIdAndNotEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndIdNotAndEliminadoFalse("Localidad", 999L);
            assertThat(result).isTrue();
        }

        @Test
        void returnsFalseWhenSameNameAndDifferentIdAndEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(true).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndIdNotAndEliminadoFalse("Localidad", 999L);
            assertThat(result).isFalse();
        }

        @Test
        void returnsFalseWhenSameNameAndSameId() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            loc = localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndIdNotAndEliminadoFalse("Localidad", loc.getId());
            assertThat(result).isFalse();
        }

        @Test
        void returnsFalseWhenDifferentName() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            localidadRepository.save(loc);

            boolean result = localidadRepository.existsByDenominacionAndIdNotAndEliminadoFalse("Other", 999L);
            assertThat(result).isFalse();
        }
    }

    @Nested
    class FindAllByEliminadoFalseOrderByDenominacion {
        @Test
        void returnsOnlyNonEliminadoOrdered() {
            Localidad loc1 = Localidad.builder().denominacion("B").eliminado(false).build();
            Localidad loc2 = Localidad.builder().denominacion("A").eliminado(false).build();
            Localidad loc3 = Localidad.builder().denominacion("C").eliminado(true).build();
            localidadRepository.saveAll(List.of(loc1, loc2, loc3));

            List<Localidad> result = localidadRepository.findAllByEliminadoFalseOrderByDenominacion();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDenominacion()).isEqualTo("A");
            assertThat(result.get(1).getDenominacion()).isEqualTo("B");
        }
    }

    @Nested
    class FindByIdAndEliminadoFalse {
        @Test
        void returnsEntityWhenNotEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(false).build();
            localidadRepository.save(loc);

            Optional<Localidad> result = localidadRepository.findByIdAndEliminadoFalse(loc.getId());

            assertThat(result).isPresent();
            assertThat(result.get().getDenominacion()).isEqualTo("Localidad");
        }

        @Test
        void returnsEmptyWhenEliminado() {
            Localidad loc = Localidad.builder().denominacion("Localidad").eliminado(true).build();
            localidadRepository.save(loc);

            Optional<Localidad> result = localidadRepository.findByIdAndEliminadoFalse(loc.getId());

            assertThat(result).isEmpty();
        }

        @Test
        void returnsEmptyWhenNotExists() {
            Optional<Localidad> result = localidadRepository.findByIdAndEliminadoFalse(999L);

            assertThat(result).isEmpty();
        }
    }
}
