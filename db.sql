CREATE DATABASE contactsapi;

DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS contact;

CREATE TABLE contact (
    id_contact BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    telefone VARCHAR(20)
);

CREATE TABLE address (
    id_address BIGINT AUTO_INCREMENT PRIMARY KEY,
    rua VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(10),
    contact_id_contact BIGINT,
    CONSTRAINT fk_address_contact
    FOREIGN KEY (contact_id_contact) REFERENCES contact(id_contact)
    ON DELETE CASCADE
);

select * from contact;
