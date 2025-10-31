package com.example.bookservice.service;

import com.example.bookservice.dto.BookRequest;
import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.exception.DuplicateIsbnException;
import com.example.bookservice.exception.ResourceNotFoundException;
import com.example.bookservice.mapper.BookMapper;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    @Retryable(maxAttempts = 3)
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException("ISBN already exists: " + request.getIsbn());
        }

        Book book = bookMapper.toEntity(request);
        book = bookRepository.save(book);
        log.info("Created book with ID: {}", book.getId());
        return bookMapper.toResponse(book);
    }

    @Cacheable(value = "books", key = "#id")
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @CachePut(value = "books", key = "#id")
    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        bookMapper.updateBookFromRequest(request, book);
        book = bookRepository.save(book);
        log.info("Updated book with ID: {}", id);
        return bookMapper.toResponse(book);
    }

    @CacheEvict(value = "books", key = "#id")
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Deleted book with ID: {}", id);
    }
}