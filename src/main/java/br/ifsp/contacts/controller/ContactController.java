package br.ifsp.contacts.controller;

import java.util.ArrayList;
import java.util.List;

import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.util.Patcher;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.ContactRepository;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private Patcher patcher;

    @GetMapping    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
    }

    @GetMapping("/name/{name}")
    public Contact getContactByName(@PathVariable String name) {
        return contactRepository.findByNome(name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Contact createContact(@Valid @RequestBody Contact contact) {

        

        if (contact.getAddresses() != null) {
            for (Address address : contact.getAddresses()) {
                address.setContact(contact);
            }
        }

        return contactRepository.save(contact);
    }
    
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Contato não encontrado: " + id));
        
        existingContact.setNome(updatedContact.getNome());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTelefone(updatedContact.getTelefone());

        return contactRepository.save(existingContact);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Contact> patchContact(@PathVariable Long id, @RequestBody Contact incompleteContact){
        Contact existingContact = contactRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado: " + id));

        try {
            Patcher.contactPatcher(existingContact, incompleteContact);

            contactRepository.save(existingContact);
        } catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body(existingContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

}
