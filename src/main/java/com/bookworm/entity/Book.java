package com.bookworm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookFormat format;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    private String coverImageUrl;
    private Boolean isBestseller = false;
    private Boolean isNewLaunch = false;
    private Integer deliveryLeadDays = 3;

    public enum BookFormat { PAPERBACK, HARDCOVER, EBOOK }

    public Book() {}

    public String getFormattedDeliveryDate() {
        int days = this.deliveryLeadDays != null ? this.deliveryLeadDays : 3;
        if (days == 0) {
            return "Instant eBook Delivery";
        }
        LocalDate deliveryDate = LocalDate.now().plusDays(days);
        return "Delivery by " + deliveryDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM"));
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public BookFormat getFormat() { return format; }
    public void setFormat(BookFormat format) { this.format = format; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Boolean getIsBestseller() { return isBestseller; }
    public void setIsBestseller(Boolean isBestseller) { this.isBestseller = isBestseller; }
    public Boolean getIsNewLaunch() { return isNewLaunch; }
    public void setIsNewLaunch(Boolean isNewLaunch) { this.isNewLaunch = isNewLaunch; }
    public Integer getDeliveryLeadDays() { return deliveryLeadDays; }
    public void setDeliveryLeadDays(Integer deliveryLeadDays) { this.deliveryLeadDays = deliveryLeadDays; }
}
