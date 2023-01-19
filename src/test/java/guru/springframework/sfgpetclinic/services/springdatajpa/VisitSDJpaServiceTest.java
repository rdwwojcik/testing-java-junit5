package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @DisplayName("Test find all")
    @Test
    void findAll() {
        Set<Visit> visitSet = Stream.of(new Visit()).collect(Collectors.toSet());

        when(visitRepository.findAll()).thenReturn(visitSet);

        Set<Visit> allVisit = service.findAll();

        verify(visitRepository).findAll();
        assertNotNull(allVisit);
        assertEquals(1, allVisit.size());
    }

    @Test
    void findById() {

        Visit visit = new Visit();

        when(visitRepository.findById(anyLong())).thenReturn(Optional.of(visit));

        Visit foundedVisit = service.findById(anyLong());

        verify(visitRepository).findById(anyLong());
        assertNotNull(foundedVisit);
        assertEquals(visit, foundedVisit);
    }

    @Test
    void save() {

        Visit visit = new Visit();
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        Visit savedVisit = service.save(new Visit());

        verify(visitRepository).save(any(Visit.class));
        assertNotNull(savedVisit);
    }

    @Test
    void delete() {

        Visit visit = new Visit();

        service.delete(visit);

        verify(visitRepository).delete(any(Visit.class));
    }

    @Test
    void deleteById() {

        service.deleteById(anyLong());

        verify(visitRepository).deleteById(anyLong());
    }
}