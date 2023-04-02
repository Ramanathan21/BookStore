package com.ramrv.book.store.service;

import com.ramrv.book.store.db.BookRepository;
import com.ramrv.book.store.model.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/books")
public class BookStore {
    private final BookRepository bookRepository;

    private final String defaultPageSize = "2";

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new book")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Book already exists")})
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {

        if (bookRepository.findById(book.getIsbn()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already exists");
        }

        Book createdBook = bookRepository.save(book);

        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/")
    @Operation(summary = "Get all books")
    public ResponseEntity<Page<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = defaultPageSize) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);

        return addNextPageLinkIfExists(size, books);
    }

    @Nonnull
    private <T> ResponseEntity<Page<T>> addNextPageLinkIfExists(int size, Page<T> entity) {
        if (entity.hasNext()) {
            int nextPage = entity.nextPageable().getPageNumber();
            String nextUrl = ServletUriComponentsBuilder.fromCurrentRequest()
                    .queryParam("page", nextPage)
                    .queryParam("size", size)
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Link", "<" + nextUrl + ">; rel=\"next\"");
            return ResponseEntity.ok().headers(headers).body(entity);
        } else {
            return ResponseEntity.ok().body(entity);
        }
    }

    @GetMapping("/{isbn}")
    @Operation(summary = "Get a book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {

        Optional<Book> book = bookRepository.findById(isbn);

        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Delete a book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    public ResponseEntity<Void> deleteBookByIsbn(@PathVariable String isbn) {

        if (bookRepository.findById(isbn).isPresent()) {
            bookRepository.deleteById(isbn);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @GetMapping("/category")
    @Operation(summary = "Get books by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<List<Book>> getBooksByCategory(
            @RequestParam(name = "name") String name) {

        Set<Book> books = bookRepository.findBooksByCategory(name);
        return new ResponseEntity<>(new ArrayList<>(books), HttpStatus.OK);
    }

    @Operation(summary = "Get books by author name", description = "Returns a list of books by the specified author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books by the specified author",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "No books found by the specified author")
    })
    @GetMapping("/author")
    public ResponseEntity<List<Book>> getBooksByAuthorName(
            @Parameter(description = "First name of the author", required = true) @RequestParam String firstName,
            @Parameter(description = "Last name of the author", required = true) @RequestParam String lastName) {
        if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First and last name of author cannot be null or empty");
        }

        Set<Book> books = bookRepository.findBooksByAuthorName(firstName, lastName);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found by the specified author");
        }

        return ResponseEntity.ok(new ArrayList<>(books));
    }

    @Operation(summary = "Search books by name", description = "Returns a list of books matching the search criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books matching the search criteria",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "No books found matching the search criteria")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByName(
            @Parameter(description = "Name of the book to search", required = true) @RequestParam String bookName) {
        if (StringUtils.isEmpty(bookName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book name cannot be null or empty");
        }

        Set<Book> books = bookRepository.findBooksByName(bookName);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ArrayList<>(books));
    }
}
