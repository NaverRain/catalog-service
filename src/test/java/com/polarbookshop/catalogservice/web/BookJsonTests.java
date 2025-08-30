package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws Exception {
        var now = Instant.now();
        var book = new Book(12135L,"1234567890", "Title", "Author", 9.99, now, now, 21);
        var jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                        .isEqualTo(book.id());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(book.createdDate());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(book.lastModifiedDate());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(book.version());
    }


    @Test
    void testDeserialization() throws Exception {
        var instant = Instant.parse("2025-07-07T22:50:37.135029Z");
        var content = """
                {
                "id" : 12135,
                "isbn" : "1234567890", 
                "title" : "Title", 
                "author" : "Author", 
                "price" : 9.99,
                "createdDate" : "2025-07-07T22:50:37.135029Z",
                "lastModifiedDate" : "2025-07-07T22:50:37.135029Z",
                "version" : 21
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book(12135L,"1234567890", "Title", "Author", 9.99, instant, instant, 21));
    }
}