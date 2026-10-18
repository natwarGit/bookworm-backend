package com.bookworm.repository;

import com.bookworm.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    List<Book> findByIsBestsellerTrue();

    List<Book> findByIsNewLaunchTrue();

    List<Book> findByCategoryId(UUID categoryId);

    @Query("SELECT b FROM Book b WHERE " +
           "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
           "(:format IS NULL OR b.format = :format) AND " +
           "(:language IS NULL OR b.language = :language) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
           "(:query IS NULL OR b.title LIKE %:query% OR b.author.name LIKE %:query%)")
    List<Book> searchBooks(
            @Param("categoryId") UUID categoryId,
            @Param("format") Book.BookFormat format,
            @Param("language") String language,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("query") String query
    );
}
