package com.ramrv.book.store.db;

import com.ramrv.book.store.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface AuthorRepository extends CrudRepository<Author, Integer> {

    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName")
    Author findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT a FROM Author a WHERE a.lastName = :lastName")
    Author findByLastName(@Param("lastName") String lastName);

    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName and a.lastName = :lastName")
    Author findByName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
