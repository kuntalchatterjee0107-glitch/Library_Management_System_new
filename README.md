# Library Management System

An in-memory, console-based Library Management System implemented in Java. The project models real-world library operations including inventory management across multiple branches, patron borrowing and reservations, event notifications, and personalized recommendations.

The architecture emphasizes Object-Oriented Programming (OOP) concepts, SOLID principles, and enterprise design patterns.

---

## Architecture & Design Patterns

### 1. Strategy Pattern
* **Search:** The `SearchStrategy` interface decouples search algorithms from the service layer. Concrete implementations include:
  * `SearchByTitleStrategy`
  * `SearchByAuthorStrategy`
  * `SearchByIsbnStrategy`
* **Recommendations:** The `RecommendationStrategy` interface allows pluggable recommendation engines. `HistoryRecommendationStrategy` prioritizes unread books matching a patron's preferred genres or authors they previously borrowed.

### 2. Observer Pattern
* `Observer` defines the contract for notification recipients.
* `Patron` implements `Observer`. When a checked-out book with active reservations is returned, the next patron in the FIFO queue automatically receives an alert via `update(String message)`.

### 3. Layered Architecture (Separation of Concerns)
* **`entity`**: Domain models maintaining state and identity (`Book`, `Patron`, `Branch`, `LendingRecord`).
* **`repository`**: In-memory data access layer using static thread-safe collections (`ConcurrentHashMap`, `LinkedList`).
* **`service`**: Coordinates business logic, transaction validation, and pattern execution (`BookService`, `PatronService`, `BranchService`, `LendingService`).
* **`strategy` / `observer`**: Behavioral patterns isolated into dedicated packages.
* **`Main`**: Interactive CLI driver handling user input and terminal presentation.

---

## Class Diagram

```mermaid
classDiagram
    direction TB

    namespace entity {
        class Book {
            -String isbn
            -String title
            -String author
            -int publicationYear
            -String genre
            -String branchId
            -boolean isBorrowed
            +getIsbn() String
            +isBorrowed() boolean
            +setBorrowed(boolean)
        }

        class Patron {
            -String patronId
            -String name
            -String email
            -List~LendingRecord~ borrowingHistory
            -Set~String~ preferredGenres
            +addBorrowingRecord(LendingRecord)
            +addPreferredGenre(String)
            +update(String message)
        }

        class Branch {
            -String branchId
            -String name
            +getBranchId() String
            +getName() String
        }

        class LendingRecord {
            -String recordId
            -String patronId
            -Book book
            -LocalDate checkoutDate
            -LocalDate returnDate
            +setReturnDate(LocalDate)
        }
    }

    namespace observer {
        class Observer {
            <<interface>>
            +update(String message)
        }
    }

    namespace strategy {
        class SearchStrategy {
            <<interface>>
            +search(Collection~Book~, String) List~Book~
        }
        class SearchByTitleStrategy
        class SearchByAuthorStrategy
        class SearchByIsbnStrategy

        class RecommendationStrategy {
            <<interface>>
            +recommend(Patron, Collection~Book~) List~Book~
        }
        class HistoryRecommendationStrategy
    }

    namespace repository {
        class BookRepository {
            -Map~String, Book~ BOOK_STORAGE$
            +save(Book)
            +findByIsbn(String) Optional~Book~
            +findAll() List~Book~
            +delete(String) boolean
        }

        class PatronRepository {
            -Map~String, Patron~ PATRON_STORAGE$
            +save(Patron)
            +findById(String) Optional~Patron~
            +findAll() List~Patron~
        }

        class BranchRepository {
            -Map~String, Branch~ BRANCH_STORAGE$
            +save(Branch)
            +findById(String) Optional~Branch~
        }

        class LendingRepository {
            -Map~String, LendingRecord~ ACTIVE_LOANS$
            +saveActiveLoan(String, LendingRecord)
            +removeActiveLoan(String) Optional~LendingRecord~
        }

        class ReservationRepository {
            -Map~String, Queue~Patron~~ RESERVATIONS$
            +enqueue(String, Patron)
            +peek(String) Optional~Patron~
            +dequeue(String) Optional~Patron~
        }
    }

    namespace service {
        class BookService {
            -BookRepository bookRepository
            +addBook(Book)
            +updateBook(String, String, String, int, String) boolean
            +removeBook(String) boolean
            +search(SearchStrategy, String, String) List~Book~
        }

        class PatronService {
            -PatronRepository patronRepository
            +registerPatron(Patron)
            +getPatron(String) Optional~Patron~
            +addPreference(String, String) boolean
        }

        class BranchService {
            -BranchRepository branchRepository
            -BookRepository bookRepository
            +registerBranch(Branch)
            +transferBook(String, String)
        }

        class LendingService {
            -BookRepository bookRepository
            -PatronRepository patronRepository
            -LendingRepository lendingRepository
            -ReservationRepository reservationRepository
            +checkout(String, String) LendingRecord
            +returnBook(String)
            +reserveBook(String, String)
            +recommendBooks(String, RecommendationStrategy) List~Book~
        }
    }

    %% Inheritance and Implementations
    Observer <|.. Patron
    SearchStrategy <|.. SearchByTitleStrategy
    SearchStrategy <|.. SearchByAuthorStrategy
    SearchStrategy <|.. SearchByIsbnStrategy
    RecommendationStrategy <|.. HistoryRecommendationStrategy

    %% Associations
    Patron o-- LendingRecord : maintains history
    LendingRecord o-- Book : references
    BookService --> BookRepository
    PatronService --> PatronRepository
    BranchService --> BranchRepository
    BranchService --> BookRepository
    LendingService --> BookRepository
    LendingService --> PatronRepository
    LendingService --> LendingRepository
    LendingService --> ReservationRepository
    LendingService ..> SearchStrategy
    LendingService ..> RecommendationStrategy
