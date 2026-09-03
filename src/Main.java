import com.airtribe.library.entity.Book;
import com.airtribe.library.entity.Branch;
import com.airtribe.library.entity.LendingRecord;
import com.airtribe.library.entity.Patron;
import com.airtribe.library.service.BookService;
import com.airtribe.library.service.BranchService;
import com.airtribe.library.service.LendingService;
import com.airtribe.library.service.PatronService;
import com.airtribe.library.starategy.recommendation.HistoryRecommendationStrategy;
import com.airtribe.library.starategy.search.SearchByAuthorStrategy;
import com.airtribe.library.starategy.search.SearchByIsbnStrategy;
import com.airtribe.library.starategy.search.SearchByTitleStrategy;
import com.airtribe.library.starategy.search.SearchStrategy;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();
    private static final PatronService patronService = new PatronService();
    private static final BranchService branchService = new BranchService();
    private static final LendingService lendingService = new LendingService();
    public static void main(String[] args) {
        seedInitialData();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> handleAddBook();
                    case "2" -> handleSearchBooks();
                    case "3" -> handleListBooks();
                    case "4" -> handleRegisterPatron();
                    case "5" -> handleAddPatronPreference();
                    case "6" -> handleViewPatronHistory();
                    case "7" -> handleCheckoutBook();
                    case "8" -> handleReturnBook();
                    case "9" -> handleReserveBook();
                    case "10" -> handleTransferBook();
                    case "11" -> handleRecommendations();
                    case "12" -> {
                        System.out.println("Exiting Library Management System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("========== LIBRARY MANAGEMENT SYSTEM ==========");
        System.out.println("1.  Add New Book");
        System.out.println("2.  Search Books");
        System.out.println("3.  List All Books");
        System.out.println("4.  Register New Patron");
        System.out.println("5.  Add Genre Preference to Patron");
        System.out.println("6.  View Patron Borrowing History");
        System.out.println("7.  Checkout Book");
        System.out.println("8.  Return Book");
        System.out.println("9.  Reserve Book");
        System.out.println("10. Transfer Book Between Branches");
        System.out.println("11. Get Book Recommendations for Patron");
        System.out.println("12. Exit");
        System.out.println("===============================================");
    }

    private static void handleAddBook() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter Publication Year: ");
        int year = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Enter Branch ID (e.g., B1, B2): ");
        String branchId = scanner.nextLine().trim();

        Book book = new Book(isbn, title, author, year, genre, branchId);
        bookService.addBook(book);
        System.out.println("Book successfully added!");
    }

    private static void handleSearchBooks() {
        System.out.println("Search by: 1. Title | 2. Author | 3. ISBN");
        String type = scanner.nextLine().trim();
        System.out.print("Enter search term: ");
        String term = scanner.nextLine().trim();
        System.out.print("Filter by Branch ID (press ENTER to search across all): ");
        String branchId = scanner.nextLine().trim();

        SearchStrategy strategy = switch (type) {
            case "2" -> new SearchByAuthorStrategy();
            case "3" -> new SearchByIsbnStrategy();
            default -> new SearchByTitleStrategy();
        };

        List<Book> results = bookService.search(strategy, term, branchId);
        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void handleListBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in system.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private static void handleRegisterPatron() {
        System.out.print("Enter Patron ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        patronService.registerPatron(new Patron(id, name, email));
        System.out.println("Patron registered successfully!");
    }

    private static void handleAddPatronPreference() {
        System.out.print("Enter Patron ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Preferred Genre: ");
        String genre = scanner.nextLine().trim();

        if (patronService.addPreference(id, genre)) {
            System.out.println("Preference added successfully!");
        } else {
            System.out.println("Patron not found.");
        }
    }

    private static void handleViewPatronHistory() {
        System.out.print("Enter Patron ID: ");
        String id = scanner.nextLine().trim();
        Patron p = patronService.getPatron(id).orElseThrow(() -> new IllegalArgumentException("Patron not found."));

        List<LendingRecord> history = p.getBorrowingHistory();
        if (history.isEmpty()) {
            System.out.println("No borrowing history for this patron.");
        } else {
            history.forEach(System.out::println);
        }
    }

    private static void handleCheckoutBook() {
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();

        LendingRecord record = lendingService.checkout(isbn, patronId);
        System.out.println("Checkout successful: " + record);
    }

    private static void handleReturnBook() {
        System.out.print("Enter Book ISBN to return: ");
        String isbn = scanner.nextLine().trim();

        lendingService.returnBook(isbn);
        System.out.println("Book returned successfully!");
    }

    private static void handleReserveBook() {
        System.out.print("Enter Book ISBN to reserve: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();

        lendingService.reserveBook(isbn, patronId);
        System.out.println("Book reserved successfully! You will be notified when it is returned.");
    }

    private static void handleTransferBook() {
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Target Branch ID: ");
        String branchId = scanner.nextLine().trim();

        branchService.transferBook(isbn, branchId);
        System.out.println("Book transferred successfully!");
    }

    private static void handleRecommendations() {
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();

        List<Book> recommendations = lendingService.recommendBooks(patronId, new HistoryRecommendationStrategy());
        if (recommendations.isEmpty()) {
            System.out.println("No personalized recommendations available at this time.");
        } else {
            System.out.println("Recommended for you:");
            recommendations.forEach(System.out::println);
        }
    }

    private static void seedInitialData() {
        branchService.registerBranch(new Branch("B1", "Downtown Central"));
        branchService.registerBranch(new Branch("B2", "Westside Campus"));

        bookService.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Software", "B1"));
        bookService.addBook(new Book("978-0201633610", "Design Patterns", "Erich Gamma", 1994, "Software", "B1"));
        bookService.addBook(new Book("978-0451524935", "1984", "George Orwell", 1949, "Dystopian", "B2"));

        Patron alice = new Patron("P101", "Alice Johnson", "alice@example.com");
        alice.addPreferredGenre("Software");
        patronService.registerPatron(alice);
    }



}