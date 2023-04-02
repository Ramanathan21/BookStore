package com.ramrv.book.store.model;

import com.ramrv.book.store.db.BookEntityListener;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@EntityListeners(BookEntityListener.class)
public class Book {
    @Id
    @NotBlank
    private String isbn;
    @NotBlank
    private String title;
    @Nullable
    private String publisher;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Nonnull
    @Valid
    private Set<Author> authors;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_category",
            joinColumns = @JoinColumn(name = "isbn"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Nonnull
    @Valid
    private Set<Category> categories;
}
