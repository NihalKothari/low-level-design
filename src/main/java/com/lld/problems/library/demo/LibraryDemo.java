package com.lld.problems.library.demo;

import com.lld.problems.library.model.Book;
import com.lld.problems.library.model.Member;
import com.lld.problems.library.service.LibraryService;

public class LibraryDemo {

    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        library.addBook(new Book("B1", "Clean Code"));
        library.registerMember(new Member("M1", "Alice"));

        if (!library.borrowBook("B1", "M1").isSuccess()) {
            throw new IllegalStateException("Borrow failed");
        }
        System.out.println("Borrowed: " + library.getBorrowedBooks("M1").get(0).getTitle());

        if (!library.returnBook("B1", "M1").isSuccess()) {
            throw new IllegalStateException("Return failed");
        }
        System.out.println("Returned. Active loans: " + library.getBorrowedBooks("M1").size());
    }
}
