package com.lld.problems.library.model;

public class Book {

    private final String id;
    private final String title;
    private BookStatus status;
    private Member borrowedBy;

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
        this.status = BookStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BookStatus getStatus() {
        return status;
    }

    public Member getBorrowedBy() {
        return borrowedBy;
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void borrow(Member member) {
        this.borrowedBy = member;
        this.status = BookStatus.BORROWED;
    }

    public void returnCopy() {
        this.borrowedBy = null;
        this.status = BookStatus.AVAILABLE;
    }
}
