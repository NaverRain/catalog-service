package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData(){
        bookRepository.deleteAll();
        var book1 = Book.of("1234567893", "The Breaking Bad",
                "Travis Scott", 131.42, "MumbaiTD");
        var book2 = Book.of("1234567892", "Northern Lights",
                "Lyra Silvestar", 9.34, "Mumba");
        bookRepository.saveAll(List.of(book1, book2));
    }

}
