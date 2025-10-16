import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class LedgerApp {
    // Declare and instantiate the Scanner and ArrayList so that it can be used throughout entire application
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> transactions = new ArrayList<>();

    public static void main(String[] args) {
        // Calling the loadTransaction from the csv file to load the information and displayHomeMenu to get the app started
        loadTransaction();
        displayHomeMenu();
    }

    private static void displayHomeMenu() {
        // Using a while loop to make sure the display menu runs until the users chooses to exit
        boolean running = true;
        while (running) { // Print out the Home menu options
            System.out.println("\n --- Home Screen --- \n");
            System.out.println("L) View Ledger");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("X) Exit");
            System.out.println("Enter your choice: ");

            String choice = scanner.nextLine();
            // Using a switch statement to route the users input to the correct sub-method they want and have ability to switch through the screens
            switch (choice.toUpperCase()) {
                case "L":
                    displayLedgerScreen(); // Calling the ledger
                    break;
                case "D":
                    addDeposit(); // Calls the make deposit method and updates the application/ledger
                    break;
                case "P":
                    makePayment(); // Calls the make payment method and updates the application/ledger
                    break;
                case "X":
                    running = false; // Exit the program
                    System.out.println("Exiting Application");
                    break;
                default: // Used for invalid choices
                    System.out.println("Invalid choice! Please choose one of the following options: L, D, P, or X ");
            }
        }
    }

    private static void loadTransaction() {

        try { // Set up FileReader and BufferedReader
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine(); // Reads and discards the title
            bufferedReader.readLine(); // Reads and discards header

            String line;
            while ((line = bufferedReader.readLine())  != null) {
                String[] actions = line.split("\\|");
                if (actions.length == 5 ) {
                    LocalDate date = LocalDate.parse(actions[0].trim());
                    LocalTime time = LocalTime.parse(actions[1].trim());
                    String description = actions[2];
                    String type = actions[3];
                    String vendor = type; // type is the name on my csv file so it's a placeholder
                    double amount = Double.parseDouble(actions[4]);
                    if (type.equalsIgnoreCase("Debit")){
                        amount = -amount;
                    }
                    Transactions newTransaction = new Transactions(date, time, description, vendor, amount);
                    transactions.add(newTransaction);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        } catch (IOException e) {
            System.out.println("Error in Input/Output" + e);
        }

    } public static String getInput(Scanner scanner, String prompt){
        System.out.println(prompt);
        return scanner.nextLine();
    }
    private static void addDeposit() {
        System.out.println("\n Add Deposit ");
        String description = getInput(scanner, "Enter description: ");
        String vendor = getInput(scanner, "Enter vendor/source: ");
        double amount = Double.parseDouble(getInput(scanner, "Enter amount"));
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount);
        transactions.add(newTransaction);
        System.out.println("Deposit added to account!");
    }

    public static void makePayment() {
        System.out.println("\n Make Payment ");
        String description = getInput(scanner, "Enter description: ");
        String vendor = getInput(scanner, "Enter vendor/source: ");
        double amountInput = Double.parseDouble(getInput(scanner, "Enter amount"));
        double amount = -amountInput;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount);
        transactions.add(newTransaction);
        System.out.println("Payment made successfully!");
    }
    private static void saveTransaction(Transactions transactions) {
        String transactionFile = "transactions.csv";
        try (FileWriter fileWriter = new FileWriter(transactionFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        ) {

            String line = String.format("%s %s|%s |%s|%.2f",
                    transactions.getDate(),
                    transactions.getTime(),
                    transactions.getDescription(),
                    transactions.getVendor(),
                    transactions.getAmount()
            );
            printWriter.println(line);

        } catch (IOException e) {
            System.out.println("Error saving transaction to file: " + e);
        }
    }
    private static void displayLedgerScreen() {
        boolean running = true;
        while (running) {
            System.out.println("\n Ledger View \n ");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments (Debits)");
            System.out.println("R) Reports");
            System.out.println("H) Home (Main Menu)");
            System.out.println("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    displayAllTransactions();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    displayReports();
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose one the following options: A, D, P, R, or H");
            }
        }
    }
    private static void displayAllTransactions() {
        System.out.println("\n Displaying All transactions \n");
        ArrayList<Transactions> displayList = new ArrayList<>(transactions);
        Collections.sort(displayList, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions t1, Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return t2.getTime().compareTo(t1.getTime());
            }
        });
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : displayList) {
            System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }
    private static void displayDeposits() {
        System.out.println("\n Displaying Deposits \n");
        displayTransactions(true);
    }
    private static void displayPayments() {
        System.out.println("\n Displaying Payments \n");
        displayTransactions(false);
    }
    private static void displayTransactions(boolean isDeposit) {
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) {
            if (isDeposit && t.getAmount() > 0) {
                filteredList.add(t);
            } else if (!isDeposit && t.getAmount() < 0) {
                filteredList.add(t);
            }
        }
        Collections.sort(filteredList, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions t1,Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate());
                if (dateComparison != 0) return dateComparison;
                return t2.getTime().compareTo(t1.getTime());
            }});
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : filteredList) {
            System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        if (filteredList.isEmpty()) {
            System.out.println("No matching transactions found.");
        }
    }
    private static void displayReports() {
        boolean running = true;
        while (running) {
            System.out.println("\n Full Account Report \n");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("H) Home (Main Menu");
            System.out.println("Enter your choice:");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "1":
                    filterByMonthToDate();
                    break;
                case "2":
                    filterByPreviousMonth();
                    break;
                case "3":
                    filterByYearToDate();
                    break;
                case "4":
                    filterByPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a number 1-6, or H");
            }
        }
    }
    private static void displayFilteredList(ArrayList<Transactions> listToDisplay) {
        if (listToDisplay.isEmpty()) {
            System.out.println("No matching transactions found");
            return;
        }
        Collections.sort(listToDisplay, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions t1, Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return t2.getTime().compareTo(t1.getTime());
            }
        });
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : listToDisplay) {
            System.out.printf("%-12s | %10s | %-25s | %15s | %s\n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }
    private static void searchByVendor() {
        System.out.println("\n Search By Vendor \n");
        String vendorSearch = getInput(scanner, "Enter vendor name: ");
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) {
            if (t.getVendor().toUpperCase().contains(vendorSearch.toUpperCase())){
                filteredList.add(t);
            }
        }
        System.out.println("\n Results for Vendor: " + vendorSearch + " \n");
        displayFilteredList(filteredList);
    }
    private static void filterByMonthToDate() {
        System.out.println(" Month to Date Report ");
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        LocalDate endDate = today;
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate);
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }
    private static void filterByPreviousMonth() {
        System.out.println(" \n Previous Month Report \n ");
        LocalDate today = LocalDate.now();
        LocalDate firstDayofThisMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        LocalDate endDate = firstDayofThisMonth.minusDays(1);
        LocalDate startDate = endDate.withDayOfMonth(1);
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate);
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }
    private static void filterByYearToDate() {
        System.out.println(" \n Year To Date Report \n ");
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), 1, 1);
        LocalDate endDate = today;
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate);
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }
    private static void filterByPreviousYear() {
        System.out.println("\n Previous Year Report \n ");
        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate startDate = LocalDate.of(lastYear.getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(lastYear.getYear(), 12, 31);
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate);
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }
    private static ArrayList<Transactions> getTransactionsInDateRange(LocalDate startDate, LocalDate endDate){
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) {
            LocalDate transactionDate = t.getDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }
    private static void customSearch() {
        System.out.println("\n Custom Search \n ");
        String startDateInput = getInput(scanner, "Enter Start Date (YYYY-MM-DD): ");
        String endDateInput = getInput(scanner, "Enter End Date (YYYY-MM-DD): ");
        String descriptionSearch = getInput(scanner, "Enter Description (keyword):");
        String vendorSearch = getInput(scanner, "Enter Vendor Name: ");
        String amountSearch = getInput(scanner, "Enter Amount (ex,. 50.00): ");
        LocalDate startDate = null;
        LocalDate endDate = null;
        Double searchAmount = null;

        try {
            if (!startDateInput.isEmpty()) {
                startDate = LocalDate.parse(startDateInput.trim());
            }
            if (!endDateInput.isEmpty()) {
                endDate = LocalDate.parse(endDateInput.trim());
            }
            if (!amountSearch.isEmpty()) {
                searchAmount = Double.parseDouble(amountSearch.trim());
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/amount format. Please try again." + e);
            return;
        }
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) {
            boolean passesAllFilters = startDate == null || !t.getDate().isBefore(startDate);
            if (endDate != null && t.getDate().isAfter(endDate)) {
                passesAllFilters = false;
            }
            if (descriptionSearch.isEmpty() && t.getDescription().toUpperCase().contains(descriptionSearch)) {
                passesAllFilters = false;
            }
            if (vendorSearch.isEmpty() && t.getVendor().toUpperCase().contains(vendorSearch)) {
                passesAllFilters = false;
            }
            if (searchAmount != null) {
                double below = searchAmount - 0.05;
                double above = searchAmount + 0.05;
                if (t.getAmount() < below || t.getAmount() > above){
                }
            }
            if (passesAllFilters) {
                filteredList.add(t);
            }
        }
        System.out.println("\n Custom Search Results \n ");
        displayFilteredList(filteredList);
    }
}

