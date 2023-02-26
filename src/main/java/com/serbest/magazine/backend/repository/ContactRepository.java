package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    @Query("select c FROM Contact c ORDER BY c.createDateTime DESC")
    List<Contact> findAllByCreateDateTime();

    List<Contact> findByReadTrueOrderByCreateDateTimeDesc();
}
