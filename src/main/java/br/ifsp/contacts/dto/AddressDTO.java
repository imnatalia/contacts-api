package br.ifsp.contacts.dto;

import br.ifsp.contacts.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class AddressDTO {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    
    @JsonIgnore
    private ContactDTO contact;

    public AddressDTO(Address entity) {
        this.rua = entity.getRua();
        this.cidade = entity.getCidade();
        this.estado = entity.getEstado();
        this.cep = entity.getCep();
    }

    public Address toEntity() {
        return new Address(this.rua, this.cidade, this.estado, this.cep);
    }
}

