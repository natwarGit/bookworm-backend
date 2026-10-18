package com.bookworm.service;

import com.bookworm.dto.BookDetailResponse;
import com.bookworm.dto.BookResponse;
import com.bookworm.entity.Book;
import com.bookworm.exception.ResourceNotFoundException;
import com.bookworm.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> searchBooks(
            UUID categoryId,
            Book.BookFormat format,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String query,
            String sortBy
    ) {
        List<Book> books = bookRepository.searchBooks(categoryId, format, language, minPrice, maxPrice, query);

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparing(Book::getPrice));
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparing(Book::getPrice).reversed());
        }

        return books.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public BookDetailResponse getBookDetails(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        List<BookResponse> related = bookRepository.findByCategoryId(book.getCategory().getId())
                .stream()
                .filter(b -> !b.getId().equals(book.getId()))
                .limit(4)
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new BookDetailResponse(
                mapToResponse(book),
                book.getAuthor().getBio(),
                related
        );
    }

    public List<BookResponse> getRecommendedBooks() {
        return bookRepository.findAll().stream()
                .limit(4)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getBestsellers() {
        return bookRepository.findByIsBestsellerTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getNewLaunches() {
        return bookRepository.findByIsNewLaunchTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor() != null ? book.getAuthor().getName() : "Unknown Author",
                book.getCategory() != null ? book.getCategory().getName() : "General",
                book.getFormat() != null ? book.getFormat().name() : "PAPERBACK",
                book.getLanguage(),
                book.getPrice(),
                book.getStockQuantity(),
                book.getCoverImageUrl(),
                book.getIsBestseller(),
                book.getIsNewLaunch(),
                book.getFormattedDeliveryDate()
        );
    }
}
