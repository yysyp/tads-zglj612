package com.example.bookservice.controller;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "Endpoints for managing books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = BookResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content),
        @ApiResponse(responseCode = "409", description = "ISBN already exists",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a book by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = BookResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Book not found",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID of book to be retrieved")
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "List of all books",
        content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = BookResponse.class)) })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = BookResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Book not found",
            content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "ID of book to be updated")
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Book not found",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of book to be deleted")
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}