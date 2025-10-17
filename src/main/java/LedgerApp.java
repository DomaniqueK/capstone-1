import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class LedgerApp {
    // Declare and instantiate the Scanner and ArrayList so that it can be used throughout entire application
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Transactions> transactions = new ArrayList<>();
    String transactionFile = "transactions.csv";

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
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();
            // Use a switch statement to route the users input to the correct sub-method they want and have ability to switch through the screens
            switch (choice) {
                case "L":
                    displayLedgerScreen(); // Call the ledger
                    break;
                case "D":
                    addDeposit(); // Call to make deposit method and updates the application/ledger
                    break;
                case "P":
                    makePayment(); // Call to make payment method and updates the application/ledger
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
        // Set up FileReader and BufferedReader (try with resources) to read the csv file
        try (FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            bufferedReader.readLine(); // Reads and discards the title
            bufferedReader.readLine(); // Reads and discards header

            String line;
            while ((line = bufferedReader.readLine())  != null) { // Loop through each remaining line in the file
                String[] actions = line.split("\\|"); // Split the by the pipe delimiter
                if (actions.length == 5 ) { // Ensure the line has all 5 expected fields
                    Transactions newTransaction = getTransactions(actions); // Create a new Transaction object from the parsed data
                    transactions.add(newTransaction); // Add the new transaction to the list
                }
            }
        } catch (FileNotFoundException e) { // catch error if the file does not exist
            System.out.println("File not found " + e);
        } catch (IOException e) {
            System.out.println("Error in Input/Output" + e); // catch error for general Input/Output
        } catch (Exception e) {
            System.out.println("Unexpected error " + e); // catch any other error
        }
    }

    private static Transactions getTransactions(String[] actions) {
        LocalDate date = LocalDate.parse(actions[0].trim());
        LocalTime time = LocalTime.parse(actions[1].trim());
        String description = actions[2];
        String vendor = actions[3];
        double amount = Double.parseDouble(actions[4]);
        if (vendor.equalsIgnoreCase("Debit") && amount > 0){
            amount = -amount; // Convert positive amount to negative for payments/debits
        }
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount); // Create and return the new Transaction object
        return newTransaction;
    }

    public static String getInput(Scanner scanner, String prompt){
        System.out.println(prompt); // Print the prompt
        return scanner.nextLine(); // Read the user's input from the next line
    }

    private static void addDeposit() {
        System.out.println("\n Add Deposit ");
        String description = getInput(scanner, "Enter description: ");
        String vendor = getInput(scanner, "Enter vendor/source: ");
        double amount = Double.parseDouble(getInput(scanner, "Enter amount"));
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount); // Create new transaction with a positive amount (deposit)
        transactions.add(newTransaction); // Add the new transaction to the list
        saveTransaction(newTransaction); // Put the new transaction on the csv file
        System.out.println("Deposit made! Get a sweet treat!");
    }

    public static void makePayment() {
        System.out.println("\n Make Payment ");
        String description = getInput(scanner, "Enter description: ");
        String vendor = getInput(scanner, "Enter vendor/source: ");
        double amountInput = Double.parseDouble(getInput(scanner, "Enter amount")); // Get positive amount from input
        double amount = -amountInput; // convert the positive amount to a negative amount for payments/debits
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount); // Create new transaction with a negative amount (payment)
        transactions.add(newTransaction);  // Add the new transaction to the list
        saveTransaction(newTransaction); // Put the new transaction on the csv file
        System.out.println("Payment successfully made! Just lay down until you get paid.");
    }

    private static void saveTransaction(Transactions transactions) {
        String transactionFile = "transactions.csv"; // Use FileWriter when true to append the existing csv file
        try (FileWriter fileWriter = new FileWriter(transactionFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter)
        ) {

            String line = String.format("%s|%s|%s|%s|%.2f", // Format to match csv file with pipes to separate
                    transactions.getDate(),
                    transactions.getTime(),
                    transactions.getDescription(),
                    transactions.getVendor(),
                    transactions.getAmount()
            );
            printWriter.println(line); // Writes the new line to the csv file
        } catch (IOException e) { // catch file writing  errors
            System.out.println("Error saving transaction to file: " + e);
        }
    }

    private static void displayLedgerScreen() {
        boolean running = true;
        while (running) { // Loop to keep the user in the ledger view until they choose Home to get back to the Home menu
            System.out.println("\n Ledger View \n ");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments (Debits)");
            System.out.println("R) Reports");
            System.out.println("H) Home (Main Menu)");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) { // Route user to select a specific display method
                case "A":
                    displayAllTransactions(); // Show all transactions
                    break;
                case "D":
                    displayDeposits(); // Show only deposits
                    break;
                case "P":
                    displayPayments(); // Show only payments
                    break;
                case "R":
                    displayReports(); // Take you to the report menu
                    break;
                case "H":
                    running = false; // Return to Home menu
                    break;
                default:
                    System.out.println("Invalid choice. Please choose one the following options: A, D, P, R, or H");
            }
        }
    }

    private static void displayAllTransactions() {
        System.out.println("\n Displaying All transactions \n");
        ArrayList<Transactions> displayList = new ArrayList<>(transactions); // Sort the list using a custom comparator with diamond brackets
        Collections.sort(displayList, new Comparator<>() {
            @Override
            public int compare(Transactions t1, Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate()); // Sort from the newest date
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return t2.getTime().compareTo(t1.getTime()); // Sort from the newest time if the dates are the same
            }
        });
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : displayList) { // Loop through the sorted list and print each transaction
            System.out.printf("%-12s | %10s | %-25s | %15s | %.2f\n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }

    private static void displayDeposits() {
        System.out.println("\n Displaying Deposits \n");
        displayTransactions(true); // Filter amounts greater than 0
    }

    private static void displayPayments() {
        System.out.println("\n Displaying Payments \n");
        displayTransactions(false); // Filter amounts less than 0
    }

    private static void displayTransactions(boolean isDeposit) {
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) {  // Iterate over all transactions
            if (isDeposit && t.getAmount() > 0) { // Filter deposits greater than 0
                filteredList.add(t);
            } else if (!isDeposit && t.getAmount() < 0) { // Filter payments/debits less than 0
                filteredList.add(t);
            }
        }
        Collections.sort(filteredList, new Comparator<>() { // Sort the filtered list by descending date and time
            @Override
            public int compare(Transactions t1, Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate());
                if (dateComparison != 0) return dateComparison;
                return t2.getTime().compareTo(t1.getTime());
            }
        });
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : filteredList) {
            System.out.printf("%-12s | %10s | %-25s | %15s | %.2f\n", // Print list items
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        if (filteredList.isEmpty()) { // Displays message if no results are found
            System.out.println("No matching transactions found.");
        }
    }

    private static void displayReports() {
        boolean running = true;
        while (running) { // Loop to keep the user in the reports menu
            System.out.println("\n Full Account Report \n");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("H) Home (Main Menu");
            System.out.print("Enter your choice:");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) { // Route the user input to the selected report
                case "1":
                    filterByMonthToDate(); // Filter list from the first day of the current month to today
                    break;
                case "2":
                    filterByPreviousMonth(); // Filter list from previous month
                    break;
                case "3":
                    filterByYearToDate(); // Filter list from January of the current year
                    break;
                case "4":
                    filterByPreviousYear(); // Filter list from January 1st to December 31st of the previous year
                    break;
                case "5":
                    searchByVendor(); // Prompt user to search by vendor name
                    break;
                case "6":
                    customSearch(); // Prompt user to search multiple different ways
                    break;
                case "H":
                    running = false; // Return to Home menu
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a number 1-6, or H");
            }
        }
    }

    private static void displayFilteredList(ArrayList<Transactions> listToDisplay) {
        if (listToDisplay.isEmpty()) { // Check if the list has any items before proceeding
            System.out.println("No matching transactions found");
            return;
        }
        Collections.sort(listToDisplay, new Comparator<>() { // Sort the list by newest date, then time
            @Override
            public int compare(Transactions t1, Transactions t2) {
                int dateComparison = t2.getDate().compareTo(t1.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return t2.getTime().compareTo(t1.getTime());
            }
        });
        System.out.printf("%-12s | %10s | %-25s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : listToDisplay) { // Print the transactions in the filtered and sorted list
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
        String searchUpper = vendorSearch.toUpperCase(); // Convert search items to uppercase for case-insensitive comparison
        for (Transactions t : transactions) { // Iterate through all transactions
            if (t.getVendor().toUpperCase().contains(searchUpper)){ // Checks vendor name
                filteredList.add(t);
            }
        }
        System.out.println("\n Results for Vendor: " + vendorSearch + " \n");
        displayFilteredList(filteredList); // Displays results of filtered search
    }

    private static void filterByMonthToDate() {
        System.out.println(" Month to Date Report ");
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), today.getMonthValue(), 1); // Start date is the 1st of the current month
        LocalDate endDate = today; // Today's date
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate); // Get transactions within the range
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }

    private static void filterByPreviousMonth() {
        System.out.println(" \n Previous Month Report \n ");
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfThisMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        LocalDate endDate = firstDayOfThisMonth.minusDays(1); // End date is the last day of the previous month
        LocalDate startDate = endDate.withDayOfMonth(1); // Start date is the first day of the previous month
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate); // Get transaction within range
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }

    private static void filterByYearToDate() {
        System.out.println(" \n Year To Date Report \n ");
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), 1, 1); // Start date is January 1st of the current year
        LocalDate endDate = today; // Today's date
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate); // Get transactions within range
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }

    private static void filterByPreviousYear() {
        System.out.println("\n Previous Year Report \n ");
        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate startDate = LocalDate.of(lastYear.getYear(), 1, 1); // Start date is January first of the previous year
        LocalDate endDate = LocalDate.of(lastYear.getYear(), 12, 31); // End date is December 31st of the previous year
        ArrayList<Transactions> filteredList = getTransactionsInDateRange(startDate, endDate); // get transactions within range
        System.out.printf(" Filtering from %s to %s\n", startDate, endDate);
        displayFilteredList(filteredList);
    }

    private static ArrayList<Transactions> getTransactionsInDateRange(LocalDate startDate, LocalDate endDate){
        ArrayList<Transactions> filteredList = new ArrayList<>();
        for (Transactions t : transactions) { // Iterate through all transactions
            LocalDate transactionDate = t.getDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) { // Checks if transaction date is on or after the start date and on or before end date
                filteredList.add(t);
            }
        }
        return filteredList; // Return the list of transaction the specified range
    }

    private static void customSearch() {
        System.out.println("\n Custom Search \n "); // Get all potential filter areas from the user
        String startDateInput = getInput(scanner, "Enter Start Date (YYYY-MM-DD): ");
        String endDateInput = getInput(scanner, "Enter End Date (YYYY-MM-DD): ");
        String descriptionSearch = getInput(scanner, "Enter Description (keyword):");
        String vendorSearch = getInput(scanner, "Enter Vendor Name: ");
        String amountSearch = getInput(scanner, "Enter Amount (ex,. 50.00): ");
        LocalDate startDate = null;
        LocalDate endDate = null;
        Double searchAmount = null;

        try {
            if (!startDateInput.isEmpty()) { // try to parse user input into date and double objects
                startDate = LocalDate.parse(startDateInput.trim());
            }
            if (!endDateInput.isEmpty()) {
                endDate = LocalDate.parse(endDateInput.trim());
            }
            if (!amountSearch.isEmpty()) {
                searchAmount = Double.parseDouble(amountSearch.trim());
            }
        } catch (DateTimeParseException e) { // catch errors if date format is wrong
            System.out.println("Invalid date/amount format. Please try again." + e);
            return;
        }
        ArrayList<Transactions> filteredList = new ArrayList<>();

        String descSearchUpper = descriptionSearch.toUpperCase(); // Pre-processes description search for efficiency
        String venSearchUpper = vendorSearch.toUpperCase(); // Pre-processes vendor search for efficiency

        for (Transactions t : transactions) { // Loop through every transaction
            boolean passesAllFilters = true;

            if (startDate != null && t.getDate().isBefore(startDate)) { // Check date range
                passesAllFilters = false;
            }
            if (endDate != null && t.getDate().isAfter(endDate)) { // Check date range
                passesAllFilters = false;
            }
            if (!descSearchUpper.isEmpty() && !t.getDescription().toUpperCase().contains(descSearchUpper)) { // Checks description keywords - case-insensitive
                passesAllFilters = false;
            }
            if (!venSearchUpper.isEmpty() && !t.getVendor().toUpperCase().contains(venSearchUpper)) { // Checks vendor keywords - case-insensitive
                passesAllFilters = false;
            }
            if (searchAmount != null) { // Check amounts using cents
               long transactionsCents = Math.round(t.getAmount() * 100);
               long searchCents = Math.round(searchAmount * 100);
               if (transactionsCents != searchCents) {
                   passesAllFilters = false;
               }
            }
            if (passesAllFilters) { // If the transaction passes all the checks, add it to the list
                filteredList.add(t);
            }
        }
        System.out.println("\n Custom Search Results \n ");
        displayFilteredList(filteredList);
    }
}

