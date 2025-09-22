package br.ifsp.contacts.util;

import br.ifsp.contacts.dto.ContactDTO;
import br.ifsp.contacts.model.Contact;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    public static void contactPatcher(Contact existingContact, ContactDTO incompleteContact) throws IllegalAccessException {
       if (incompleteContact.getNome() != null) {
            existingContact.setNome(incompleteContact.getNome());
        }

        if (incompleteContact.getEmail() != null) {
            existingContact.setEmail(incompleteContact.getEmail());
        }

        if (incompleteContact.getTelefone() != null) {
            existingContact.setTelefone(incompleteContact.getTelefone());
        }

        Class<?> contactClass = Contact.class;
        Field[] contactFields = contactClass.getDeclaredFields();

        for(Field field : contactFields){
            field.setAccessible(true);

            Object value=field.get(incompleteContact);
            if(value!=null){
                field.set(existingContact,value);
            }

            field.setAccessible(false);
        }

    }

}
