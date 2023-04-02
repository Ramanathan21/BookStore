package com.ramrv.book.store;

import com.ramrv.book.store.db.AuthorRepository;
import com.ramrv.book.store.db.BookRepository;
import com.ramrv.book.store.db.CategoryRepository;
import com.ramrv.book.store.model.Author;
import com.ramrv.book.store.model.Book;
import com.ramrv.book.store.model.Category;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class Application {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void test() {
        log.info("----------------find book by id----------------");
        var foundBook = bookRepository.findById("81-7992-192-X");
        if (foundBook.isEmpty()) {
            log.info("Book doesn't exist");
        } else {
            log.info("Found book: {}", foundBook.get());
        }

        log.info("---------------- Get all Books ----------------");
        Iterable<Book> booksFound = bookRepository.findAll();
        booksFound.forEach(book -> log.info("Book is: {}", book));

        log.info("----------------Retrieve existing Authors from DB----------------");
        Author author = new Author();
        author.setFirstName("Ralph");
        author.setLastName("Johnson");

        var authorFromDB = authorRepository.findByName(author.getFirstName(), author.getLastName());
        if (authorFromDB != null) {
            author = authorFromDB;
        }

        log.info("----------------Retrieve existing categories from DB----------------");
        Category category = categoryRepository.findByName("Technical");
        if (category == null) {
            category = new Category();
            category.setName("Technical");
        }
        Category category1 = categoryRepository.findByName("Humour");
        if (category1 == null) {
            category1 = new Category();
            category1.setName("Humour");
        }

        Book book = new Book();
        book.setAuthors(Set.of(author));
        book.setCategories(Set.of(category1));
        book.setIsbn("19191");
        book.setTitle("Atomic Habits");
        book.setPublisher("Penguin");

        log.info("----------------save book----------------");
        var savedInstance = bookRepository.save(book);
        log.info("{} is saved as {}", book, savedInstance);

        log.info("----------------check if book is saved----------------");
        var bookIsSaved = bookRepository.existsById("19191");
        log.info("is book saved? {}", bookIsSaved);

        log.info("----------------get book by id----------------");
        foundBook = bookRepository.findById("19191");
        if (foundBook.isEmpty()) {
            log.info("Book doesn't exist");
        } else {
            log.info("Found book: {}", foundBook.get());
        }

        log.info("----------------get book by category - Technical---------------");
        booksFound = bookRepository.findBooksByCategory("Technical");
        booksFound.forEach(b -> log.info("For category Technical: Book is: {}", b));

        log.info("----------------get book by category - Humour---------------");
        booksFound = bookRepository.findBooksByCategory("Humour");
        booksFound.forEach(b -> log.info("For category Humour: Book is: {}", b));

        log.info("----------------find book by author----------------");
        booksFound = bookRepository.findBooksByAuthorName("Ralph", "Johnson");
        booksFound.forEach(b -> log.info("Book is: {} ", b));

        log.info("----------------find book by name----------------");
        booksFound = bookRepository.findBooksByName("Atomic Habits");
        booksFound.forEach(b -> log.info("Book is: {} ", b));

        log.info("----------------delete a book----------------");
        bookRepository.delete(book);

        log.info("----------------check if book exist after deletion----------------");
        var bookExist = bookRepository.existsById("19191");
        log.info("is book exist? {}", bookExist);

        log.info("----------------get book by category - Technical - Check the other tech book exists ---------------");
        booksFound = bookRepository.findBooksByCategory("Technical");
        booksFound.forEach(b -> log.info("For category Technical: Book is: {}", b));

        log.info("----------------get book by category - Humour - Check no book exists as its deleted ---------------");
        booksFound = bookRepository.findBooksByCategory("Humour");
        if (!booksFound.iterator().hasNext()) {
            log.info("No book exist!");
        }

        log.info("----------------find book by author - check previous book exist for author ----------------");
        booksFound = bookRepository.findBooksByAuthorName("Ralph", "Johnson");
        booksFound.forEach(b -> log.info("Book is: {} ", b));
    }
}
