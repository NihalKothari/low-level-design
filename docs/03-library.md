# Problem 03: Library Management

## Problem Statement

Design a library system that manages a catalog of books and registered members. Members can borrow available books and return them, with the system tracking who holds each copy.

## Functional Requirements

1. Register books and members in the catalog
2. Allow a member to borrow an available book (one copy per member per book)
3. Allow a member to return a borrowed book
4. Reject borrow when the book is already checked out or member/book not found
5. List books currently borrowed by a member

## Out of Scope

- Database / persistence
- REST APIs / Spring Boot
- Distributed deployment
- Late fees and reservation queues

## Class Diagram

```mermaid
classDiagram
    class LibraryService {
        +addBook(Book)
        +registerMember(Member)
        +borrowBook(String bookId, String memberId)
        +returnBook(String bookId, String memberId)
    }
    class Book {
        +getId()
        +isAvailable()
        +borrow(Member)
        +returnCopy()
    }
    class Member {
        +getId()
        +getName()
    }
    class BookStatus
    LibraryService --> Book
    LibraryService --> Member
    Book --> BookStatus
    Book --> Member : borrowedBy
```

## Sequence Diagram (Key Flow)

```mermaid
sequenceDiagram
    participant Member
    participant LibraryService
    participant Book
    Member->>LibraryService: borrowBook("B1", "M1")
    LibraryService->>Book: isAvailable()?
    Book-->>LibraryService: true
    LibraryService->>Book: borrow(member)
    LibraryService-->>Member: success
```

## API Surface

| Method | Description |
|--------|-------------|
| `addBook(Book)` | Add book to catalog |
| `registerMember(Member)` | Register library member |
| `borrowBook(bookId, memberId)` | Checkout book to member |
| `returnBook(bookId, memberId)` | Return borrowed book |
| `getBorrowedBooks(memberId)` | List member's active loans |

## Design Patterns

- **Repository** (extension): abstract book/member storage
- **Factory**: create `Book` instances with ISBN validation
- **Observer** (extension): notify when reserved book becomes available

## Extension Questions

1. How would you add a waitlist when a book is unavailable?
2. How do you enforce borrowing limits per member tier?
3. How would you support multiple copies of the same title?

## Package

`com.lld.problems.library`

## Test Class

`LibraryTest`
