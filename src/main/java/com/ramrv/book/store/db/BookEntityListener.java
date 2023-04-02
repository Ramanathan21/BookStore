package com.ramrv.book.store.db;

import com.ramrv.book.store.model.Author;
import com.ramrv.book.store.model.Book;
import com.ramrv.book.store.model.Category;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * To avoid duplication of categories and authors, when a book is saved, check if these exist and use them.
 */
@Slf4j
@Component
public class BookEntityListener {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @PrePersist
    @PreUpdate
    public void checkCategoryAndAuthor(Book book) {
        checkCategories(book);
        checkAuthors(book);
    }

    void checkCategories(Book book) {
        Set<Category> categories = book.getCategories();
        if (categories == null) return;
        Set<Category> existingCategories = new HashSet<>();
        for (Category category : categories) {
            if (categoryRepository == null) return;
            Category existingCategory = categoryRepository.findByName(category.getName());
            if (existingCategory != null) {
                log.info("Category {} already exists. Getting it from DB", existingCategory);
                existingCategories.add(existingCategory);
            } else {
                log.info("Category {} doesn't exist. Save it", category);
                categoryRepository.save(category);
                existingCategories.add(category);
            }
        }
        book.setCategories(existingCategories);
    }

    void checkAuthors(Book book) {
        Set<Author> authors = book.getAuthors();
        if (authors == null) return;
        Set<Author> existingAuthors = new HashSet<>();
        for (Author author : authors) {
            if (authorRepository == null) return;
            Author existingAuthor = authorRepository.findByName(author.getFirstName(), author.getLastName());
            if (existingAuthor != null) {
                log.info("Author {} already exists. Getting it from DB", existingAuthor);
                existingAuthors.add(existingAuthor);
            } else {
                log.info("Author {} doesn't exist. Save it", author);
                authorRepository.save(author);
                existingAuthors.add(author);
            }
        }
        book.setAuthors(existingAuthors);
    }
}

