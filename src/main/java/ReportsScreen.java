import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReportsScreen {
    public static void displayReports() {
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

            String choice = LedgerApp.scanner.nextLine().toUpperCase();

            switch (choice) { // Route the user input to the selected report
                case "1" -> filterByMonthToDate(); // Filter list from the first day of the current month to today
                case "2" -> filterByPreviousMonth(); // Filter list from previous month
                case "3" -> filterByYearToDate(); // Filter list from January of the current year
                case "4" -> filterByPreviousYear(); // Filter list from January 1st to December 31st of the previous year
                case "5" -> searchByVendor(); // Prompt user to search by vendor name
                case "6" -> customSearch(); // Prompt user to search multiple different ways
                case "H" -> {
                    running = false;
                } // Return to Home menu

                default -> System.out.println("Invalid choice! Please choose a number 1-6, or H");
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
        System.out.printf("%-12s | %-10s | %-25s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : listToDisplay) { // Print the transactions in the filtered and sorted list
            System.out.printf("%-12s | %-10s | %-25s | %15s | %s\n",
                    t.getDate(),
                    t.getTime().format(LedgerApp.formatter),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }

    private static void searchByVendor() {
        System.out.println("\n Search By Vendor \n");
        String vendorSearch = LedgerApp.getInput(LedgerApp.scanner, "Enter vendor name: ");
        ArrayList<Transactions> filteredList = new ArrayList<>();
        String searchUpper = vendorSearch.toUpperCase(); // Convert search items to uppercase for case-insensitive comparison
        for (Transactions t : LedgerApp.transactions) { // Iterate through all transactions
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
        for (Transactions t : LedgerApp.transactions) { // Iterate through all transactions
            LocalDate transactionDate = t.getDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) { // Checks if transaction date is on or after the start date and on or before end date
                filteredList.add(t);
            }
        }
        return filteredList; // Return the list of transaction the specified range
    }

    private static void customSearch() {
        System.out.println("\n Custom Search \n "); // Get all potential filter areas from the user
        String startDateInput = LedgerApp.getInput(LedgerApp.scanner, "Enter Start Date (YYYY-MM-DD): ");
        String endDateInput = LedgerApp.getInput(LedgerApp.scanner, "Enter End Date (YYYY-MM-DD): ");
        String descriptionSearch = LedgerApp.getInput(LedgerApp.scanner, "Enter Description (keyword):");
        String vendorSearch = LedgerApp.getInput(LedgerApp.scanner, "Enter Vendor Name: ");
        String amountSearch = LedgerApp.getInput(LedgerApp.scanner, "Enter Amount (ex,. 50.00): ");
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

        for (Transactions t : LedgerApp.transactions) { // Loop through every transaction
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
