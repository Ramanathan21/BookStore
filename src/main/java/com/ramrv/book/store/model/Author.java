package com.ramrv.book.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    @JsonIgnore
    private int authorId;
    @Column(name = "first_name")
    @NotBlank
    private String firstName;
    @Column(name = "last_name")
    @NotBlank
    private String lastName;
}
