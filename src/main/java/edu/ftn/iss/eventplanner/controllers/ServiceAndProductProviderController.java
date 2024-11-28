package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ServiceAndProductProviderController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetProviderDTO>> getProviders() {
        Collection<GetProviderDTO> providers = new ArrayList<>();

        GetProviderDTO provider1 = new GetProviderDTO();
        provider1.setCompanyName("Event Co.");
        provider1.setDescription("We provide top-notch event services.");
        provider1.setAddress("123 Event Street");
        provider1.setPhoneNumber("123-456-7890");
        List<String> photos1 = Arrays.asList("photo1.jpg", "photo2.jpg", "photo3.jpg");
        provider1.setPhotos(photos1);
        provider1.setCategories(new String[]{"Catering", "Photography"});
        provider1.setEventTypes(new String[]{"Weddings", "Corporate"});

        GetProviderDTO provider2 = new GetProviderDTO();
        provider2.setCompanyName("Event2 Co.");
        provider2.setDescription("We provide top-notch event services.");
        provider2.setAddress("123 Event Street");
        provider2.setPhoneNumber("123-456-7890");
        List<String> photos2 = Arrays.asList("photo1.jpg", "photo2.jpg", "photo3.jpg");
        provider2.setPhotos(photos2);
        provider2.setCategories(new String[]{"Catering", "Photography"});
        provider2.setEventTypes(new String[]{"Weddings", "Corporate"});

        providers.add(provider1);
        providers.add(provider2);

        return new ResponseEntity<Collection<GetProviderDTO>>(providers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetProviderDTO> getProvider(@PathVariable("id") Long id) {
        GetProviderDTO provider = new GetProviderDTO();

        if (provider == null) {
            return new ResponseEntity<GetProviderDTO>(HttpStatus.NOT_FOUND);
        }

        provider.setCompanyName("Event Co.");
        provider.setDescription("We provide top-notch event services.");
        provider.setAddress("123 Event Street");
        provider.setPhoneNumber("123-456-7890");
        List<String> photos = Arrays.asList("photo1.jpg", "photo2.jpg", "photo3.jpg");
        provider.setPhotos(photos);
        provider.setCategories(new String[]{"Catering", "Photography"});
        provider.setEventTypes(new String[]{"Weddings", "Corporate"});

        return new ResponseEntity<GetProviderDTO>(provider, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedProviderDTO> updateProvider(@PathVariable Long id, @RequestBody UpdateProviderDTO provider) throws Exception{
        UpdatedProviderDTO updatedProvider = new UpdatedProviderDTO();

        updatedProvider.setId(id);
        updatedProvider.setDescription(provider.getDescription());
        updatedProvider.setAddress(provider.getAddress());
        updatedProvider.setPhoneNumber(provider.getPhoneNumber());
        updatedProvider.setPhotos(provider.getPhotos());

        return new ResponseEntity<UpdatedProviderDTO>(updatedProvider, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangedPasswordDTO> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO password) throws Exception {
        ChangedPasswordDTO changedPassword = new ChangedPasswordDTO();

        changedPassword.setId(id);
        changedPassword.setOldPassword(password.getOldPassword());
        changedPassword.setNewPassword(password.getNewPassword());
        changedPassword.setConfirmNewPassword(password.getConfirmNewPassword());

        return new ResponseEntity<ChangedPasswordDTO>(changedPassword, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deactivateProvider(@PathVariable Long id) {
        DeactivateAccountDTO account = new DeactivateAccountDTO();

        account.setActive(false);

        return new ResponseEntity<>("Provider account deactivated.", HttpStatus.OK);
    }
}
