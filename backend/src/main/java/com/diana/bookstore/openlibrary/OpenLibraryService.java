package com.diana.bookstore.openlibrary;

import com.diana.bookstore.book.OpenLibraryBookDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OpenLibraryService {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://openlibrary.org")
            .build();

    public List<OpenLibraryBookDto> searchBook(String query) {
        OpenLibraryResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("author", query)
                        .queryParam("limit", 3)
                        .build())
                .retrieve()
                .bodyToMono(OpenLibraryResponse.class)
                .block();
        return response.docs().stream()
                .map(doc -> new OpenLibraryBookDto(
                        null,
                        doc.coverId() != null ? "https://covers.openlibrary.org/b/id/" + doc.coverId() + "-M.jpg" : null,
                        doc.title(),
                        doc.author() != null && !doc.author().isEmpty() ? doc.author().get(0) : "Unknown",
                        doc.year(),
                        null,
                        null
                ))
                .toList();
    }
}
