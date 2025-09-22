package br.ifsp.contacts.dto;

import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.model.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class ContactDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "O email deve ser válido")
    private String email;

    @Size(min = 8, max = 15, message = "O telefone deve ter entre 8 e 15 dígitos")
    private String telefone;

    private List<AddressDTO> addresses = new ArrayList<>();

    public ContactDTO() {
    }

    public ContactDTO(Contact entity) {
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.telefone = entity.getTelefone();
        this.addresses = entity.getAddresses().stream()
                .map(AddressDTO::new)
                .collect(Collectors.toList());
    }

    public Contact toEntity() {
        Contact contact = new Contact(this.nome, this.email, this.telefone);

        if (this.addresses != null) {
            for (AddressDTO addressDTO : this.addresses) {
                Address addressEntity = addressDTO.toEntity();
                addressEntity.setContact(contact);
                contact.getAddresses().add(addressEntity);
            }
        }
        return contact;
    }
}
