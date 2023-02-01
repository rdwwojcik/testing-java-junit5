package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";
    @Mock
    OwnerService ownerService;

    @Mock
    BindingResult bindingResult;

    @Mock
    Model model;

    @InjectMocks
    OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp(){
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> owners = new ArrayList<>();

                    String name = invocation.getArgument(0);
                    if(name.equals("%Back%")){
                        owners.add(new Owner(1L, "Joe", "Back"));
                        return owners;
                    } else if (name.equals("%DontFindMe%")) {
                        return owners;
                    } else if (name.equals("%FindMe%")) {
                        owners.add(new Owner(1L, "Joe", "Back"));
                        owners.add(new Owner(2L, "Joe2", "Back2"));
                        return owners;
                    }

                    throw  new RuntimeException("Invalid Argument");
                });
    }

    @Test
    void processFindFormWildcardAnnotation() {
        //given
        Owner owner = new Owner(1L, "Joe", "Back");
//        List<Owner> ownerList = new ArrayList<>();
//        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willReturn(ownerList);
        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);
        //then
        assertEquals("%Back%", stringArgumentCaptor.getValue());
        assertEquals("redirect:/owners/1", viewName);
    }

    @Test
    void processFindFormWildcardNotFound() {
        //given
        Owner owner = new Owner(1L, "Joe", "DontFindMe");
        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);
        //then
        assertEquals("%DontFindMe%", stringArgumentCaptor.getValue());
        assertEquals("owners/findOwners", viewName);
    }


    @Test
    void processFindFormWildcardFound() {
        //given
        Owner owner = new Owner(1L, "Joe", "FindMe");
        InOrder inOrder = Mockito.inOrder(ownerService, model);
        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, model);
        //then
        assertEquals("%FindMe%", stringArgumentCaptor.getValue());
        assertEquals("owners/ownersList", viewName);
        //inorder asserts
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
    }

    @Test
    void processFindFormWildcardString() {
        //given
        Owner owner = new Owner(1L, "Joe", "Back");
        List<Owner> ownerList = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(ownerList);
        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);
        //then
        assertEquals("%Back%", captor.getValue());
    }

    @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(5L, "Radek", "Wójcik");
        given(bindingResult.hasErrors()).willReturn(Boolean.TRUE);
        //when
        String savedResult = ownerController.processCreationForm(owner, bindingResult);
        //then
        assertEquals(OWNERS_CREATE_OR_UPDATE_OWNER_FORM, savedResult);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5L, "Radek", "Wójcik");
        given(ownerService.save(argThat(matcher -> matcher.getId().equals(5L)))).willReturn(owner);
        given(bindingResult.hasErrors()).willReturn(Boolean.FALSE);
        //when
        String savedResult = ownerController.processCreationForm(owner, bindingResult);
        //then
        assertEquals(REDIRECT_OWNERS_5, savedResult);
    }
}