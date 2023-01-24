package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceBDDTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        //given
        Set<Visit> visitSet = Stream.of(new Visit()).collect(Collectors.toSet());
        given(visitRepository.findAll()).willReturn(visitSet);

        //when
        Set<Visit> allVisit = service.findAll();

        //then
        then(visitRepository).should().findAll();
        then(visitRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findById() {
        //given
        Visit visit = new Visit(1L);
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));

        //when
        Visit byId = service.findById(anyLong());

        //then
        then(visitRepository).should().findById(anyLong());
        then(visitRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit(1L);
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        //when
        Visit savedVisit = service.save(visit);

        //then
        then(visitRepository).should().save(any(Visit.class));
        assertNotNull(savedVisit);
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit(1L);

        //when
        service.delete(visit);

        //then
        then(visitRepository).should().delete(any(Visit.class));
        then(visitRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        //when
        service.deleteById(1L);

        //then
        then(visitRepository).should().deleteById(anyLong());
        then(visitRepository).shouldHaveNoMoreInteractions();
    }
}