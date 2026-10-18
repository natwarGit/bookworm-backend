package com.bookworm.controller;

import com.bookworm.dto.BookDetailResponse;
import com.bookworm.dto.BookResponse;
import com.bookworm.entity.Book;
import com.bookworm.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Catalogue", description = "Endpoints for browsing, filtering, and searching books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Browse and filter catalogue books with faceted search")
    public ResponseEntity<List<BookResponse>> getBooks(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Book.BookFormat format,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "relevance") String sortBy
    ) {
        return ResponseEntity.ok(bookService.searchBooks(categoryId, format, language, minPrice, maxPrice, query, sortBy));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get single book details with tentative delivery date and related titles")
    public ResponseEntity<BookDetailResponse> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookDetails(id));
    }

    @GetMapping("/recommended")
    @Operation(summary = "Get personalized book recommendations")
    public ResponseEntity<List<BookResponse>> getRecommendedBooks() {
        return ResponseEntity.ok(bookService.getRecommendedBooks());
    }

    @GetMapping("/bestsellers")
    @Operation(summary = "Get bestsellers this month")
    public ResponseEntity<List<BookResponse>> getBestsellers() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/new-launches")
    @Operation(summary = "Get newly launched books")
    public ResponseEntity<List<BookResponse>> getNewLaunches() {
        return ResponseEntity.ok(bookService.getNewLaunches());
    }
}
