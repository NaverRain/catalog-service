package com.polarbookshop.catalogservice.domain;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds(){
        var book =
                Book.of("1234567890", "Title", "Author", 9.33);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails(){
        var book =
                Book.of("13", "Title", "Author", 9.43);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessage = violations.stream()
                        .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book ISBN must be defined")
                .isEqualTo("The ISBN format must be valid.");
    }

}
