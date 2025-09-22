package br.ifsp.contacts.controller;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.ContactRepository;
import br.ifsp.contacts.dto.AddressDTO;
import br.ifsp.contacts.dto.ContactDTO;
import br.ifsp.contacts.util.Patcher;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "API para gerenciamento de contatos")
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private Patcher patcher;

    @Operation(summary = "Lista todos os contatos")
    @ApiResponse(responseCode = "200", description = "Lista de contatos retornada com sucesso")
    @GetMapping    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Operation(summary = "Lista contatos paginados")
    @ApiResponse(responseCode = "200", description = "Lista paginada de contatos retornada com sucesso")

    @GetMapping(value = "pageable")
    public List<ContactDTO> getAllContactsPageable(@PageableDefault(sort = "nome", direction = Sort.Direction.ASC) @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return contactRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(ContactDTO::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Busca contato por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato encontrado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content)
    })
    @GetMapping("/id/{id}")
    public ContactDTO getContactById(@PathVariable Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
        return new ContactDTO(contact);
    }

    @Operation(summary = "Busca contato por nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato encontrado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ContactDTO getContactByName(@PathVariable String name) {
        Contact contact = contactRepository.findByNome(name);
        if (contact != null) {
            return new ContactDTO(contact);
        }
        throw new RuntimeException("Contato não encontrado: " + name);
    }

    @Operation(summary = "Cria um novo contato")
    @ApiResponse(responseCode = "201", description = "Contato criado com sucesso")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ContactDTO createContact(@Valid @RequestBody ContactDTO contactDTO) {
        if (contactDTO.getAddresses() != null) {
            for (AddressDTO address : contactDTO.getAddresses()) {
                address.setContact(contactDTO);
            }
        }

        Contact contact = contactDTO.toEntity();
        contactRepository.save(contact);
        return new ContactDTO(contact);
    }

    @Operation(summary = "Atualiza um contato existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ContactDTO updateContact(@PathVariable Long id, @RequestBody ContactDTO updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Contato não encontrado: " + id));
            
        existingContact.setNome(updatedContact.getNome());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTelefone(updatedContact.getTelefone());
        
        contactRepository.save(existingContact);
        
        ContactDTO existingContactDTO = new ContactDTO(existingContact);
        return existingContactDTO;
    }

    @Operation(summary = "Atualiza parcialmente um contato")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content)
    })
    @PatchMapping("/patch/{id}")
    public ResponseEntity<ContactDTO> patchContact(@PathVariable Long id, @RequestBody ContactDTO incompleteContact){
        Contact existingContact = contactRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado: " + id));

        try {
            Patcher.contactPatcher(existingContact, incompleteContact);

            contactRepository.save(existingContact);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        ContactDTO contactDTO = new ContactDTO(existingContact);

        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

    @Operation(summary = "Remove um contato")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contato removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

}
