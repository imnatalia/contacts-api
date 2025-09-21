package br.ifsp.contacts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.contacts.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByNome(String nome);
}
