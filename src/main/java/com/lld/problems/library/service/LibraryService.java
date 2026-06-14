package com.lld.problems.library.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.library.model.Book;
import com.lld.problems.library.model.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryService {

    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getId(), member);
    }

    public Result<Void> borrowBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);
        if (book == null || member == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!book.isAvailable()) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        book.borrow(member);
        return Result.success(null);
    }

    public Result<Void> returnBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);
        if (book == null || member == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (book.getBorrowedBy() == null || !book.getBorrowedBy().getId().equals(memberId)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        book.returnCopy();
        return Result.success(null);
    }

    public List<Book> getBorrowedBooks(String memberId) {
        return books.values().stream()
                .filter(b -> b.getBorrowedBy() != null && b.getBorrowedBy().getId().equals(memberId))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
