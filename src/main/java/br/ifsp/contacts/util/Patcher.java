package br.ifsp.contacts.util;

import br.ifsp.contacts.model.Contact;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    public static void contactPatcher(Contact existingContact, Contact incompleteContact) throws IllegalAccessException {
        Class<?> contactClass = Contact.class;
        Field[] contactFields = contactClass.getDeclaredFields();
        System.out.println(contactFields.length);

        for(Field field : contactFields){
            System.out.println(field.getName());
            field.setAccessible(true);

            Object value=field.get(incompleteContact);
            if(value!=null){
                field.set(existingContact,value);
            }

            field.setAccessible(false);
        }

    }

}
