package com.ramrv.book.store.db;

import com.ramrv.book.store.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b JOIN FETCH b.categories c LEFT JOIN FETCH b.authors a WHERE b.title = :bookName")
    Set<Book> findBooksByName(@Param("bookName") String bookName);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories c LEFT JOIN FETCH b.authors a WHERE c.name = :categoryName")
    Set<Book> findBooksByCategory(@Param("categoryName") String categoryName);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors a LEFT JOIN FETCH b.categories c WHERE a.firstName = :firstName and a.lastName = :lastName")
    Set<Book> findBooksByAuthorName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
