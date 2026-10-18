package com.bookworm.dto;

import java.util.List;

public record BookDetailResponse(
    BookResponse book,
    String authorBio,
    List<BookResponse> relatedBooks
) {}
