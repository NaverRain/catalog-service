package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;


    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1111111111";
        var book = Book.of(bookIsbn, "Title 1", "Author 1", 12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    void findAllBooks(){
        var book1 = Book.of("2222222222", "Title 1", "Author 1", 123.132, "Publisher 1");
        var book2 = Book.of("3333333333", "Title 2", "Author 2", 123.123, "Publisher 2");

        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<Book> books = bookRepository.findAll();

        assertThat(StreamSupport.stream(books.spliterator(), true)
                .filter(book -> book.isbn().equals(book1.isbn()) || book.isbn().equals(book2.isbn()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void deleteByIsbn(){
        var bookIsbn = "4444444444";
        var bookToCreate = Book.of(bookIsbn, "Title 4", "Author 4", 123.44, "Publisher 4");

        var persistedBook = jdbcAggregateTemplate.insert(bookToCreate);

        bookRepository.deleteByIsbn(bookIsbn);
        assertThat(jdbcAggregateTemplate.findById(persistedBook.id(), Book.class)).isNull();
    }

    @Test
    void existsByIsbnWhenExisting(){
        var bookIsbn = "5555555555";
        var bookToCreate = Book.of(bookIsbn, "Title 5", "Author 5", 213.32, "Publisher 5");
        jdbcAggregateTemplate.insert(bookToCreate);

        boolean isExisting = bookRepository.existsByIsbn(bookIsbn);

        assertThat(isExisting).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting(){
        boolean isExisting = bookRepository.existsByIsbn("6666666666");

        assertThat(isExisting).isFalse();
    }
}