package com.erpexample.service;

import com.erpexample.entity.Contact;
import com.erpexample.repository.ContactIdGeneratorRepository;
import com.erpexample.repository.ContactRepository;
import dml.id.entity.SnowflakeIdGenerator;
import erp.annotation.Process;
import erp.repository.factory.RepositoryFactory;
import erp.repository.factory.SingletonRepositoryFactory;
import erp.springjdbc.mysql.MySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AddressBookService {

    private ContactRepository contactRepository;
    private ContactIdGeneratorRepository contactIdGeneratorRepository;

    @Autowired
    public AddressBookService(JdbcTemplate jdbcTemplate) {
        contactRepository = RepositoryFactory.newInstance(ContactRepository.class,
                new MySQLRepository<>(jdbcTemplate, Contact.class));
        contactIdGeneratorRepository = SingletonRepositoryFactory.newInstance(ContactIdGeneratorRepository.class,
                new SnowflakeIdGenerator(1L) {
                });
    }

    @Process
    public void addContact(String name, String phoneNumber) {
        Contact contact = new Contact();
        contact.setId(contactIdGeneratorRepository.take().generateId());
        contact.setName(name);
        contact.setPhoneNumber(phoneNumber);
        contactRepository.put(contact);
    }

    @Process
    public void removeContact(Long id) {
        contactRepository.remove(id);
    }
}
