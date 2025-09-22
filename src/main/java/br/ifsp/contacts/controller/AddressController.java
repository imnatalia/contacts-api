package br.ifsp.contacts.controller;

import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts/{id}/addresses")
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    public List<Address> getAddressesByContactId(@PathVariable Long id) {
        return addressRepository.findByContactIdContact(id);
    }

}
