package com.lld.problems.library;

import com.lld.problems.library.model.Book;
import com.lld.problems.library.model.Member;
import com.lld.problems.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {

    private LibraryService library;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.addBook(new Book("B1", "Design Patterns"));
        library.registerMember(new Member("M1", "Bob"));
    }

    @Test
    void borrowAndReturnBook() {
        assertTrue(library.borrowBook("B1", "M1").isSuccess());
        assertEquals(1, library.getBorrowedBooks("M1").size());
        assertTrue(library.returnBook("B1", "M1").isSuccess());
        assertEquals(0, library.getBorrowedBooks("M1").size());
    }

    @Test
    void rejectBorrowWhenAlreadyCheckedOut() {
        assertTrue(library.borrowBook("B1", "M1").isSuccess());
        assertTrue(library.borrowBook("B1", "M1").getError().isPresent());
    }
}
