import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LedgerScreen {

    static double balance = 0;
    public static void displayLedgerScreen() {
        boolean running = true;
        while (running) { // Loop to keep the user in the ledger view until they choose Home to get back to the Home menu
            System.out.println("\n Ledger View \n ");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments (Debits)");
            System.out.println("R) Reports");
            System.out.println("H) Home (Main Menu)");
            System.out.print("Enter your choice: ");

            String choice = LedgerApp.scanner.nextLine().toUpperCase();

            switch (choice) { // Route user to select a specific display method
                case "A" -> displayAllTransactions(); // Show all transactions
                case "D" -> displayDeposits(); // Show only deposits
                case "P" -> displayPayments(); // Show only payments
                case "R" -> ReportsScreen.displayReports(); // Take you to the report menu
                case "H" -> {
                    running = false;
                } // Return to Home menu
                default -> System.out.println("Invalid choice. Please choose one the following options: A, D, P, R, or H");
            }
        }
    }
    public static void displayAllTransactions() {
        System.out.println("\n Displaying All transactions \n");
        ArrayList<Transactions> displayList = new ArrayList<>(LedgerApp.transactions); // Sort the list using a custom comparator with diamond brackets
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
        for(Transactions transactions: LedgerApp.transactions){
            balance += transactions.amount;
        }
        System.out.printf("%-12s | %-10s | %-25s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : displayList) { // Loop through the sorted list and print each transaction
            System.out.printf("%-12s | %-10s | %-25s | %15s | %.2f\n",
                    t.getDate(),
                    t.getTime().format(LedgerApp.formatter),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        System.out.println("Current balance:" + balance);
    }

    public static void displayDeposits() {

        double totalDepo = 0;
        System.out.println("\n Displaying Deposits \n");
        LedgerApp.displayTransactions(true);// Filter amounts greater than 0
        for(Transactions transactions: LedgerApp.transactions){
            totalDepo += transactions.amount;
        }

        System.out.println("Total: " + totalDepo);
    }

    public static void displayPayments() {

        double totalPay = 0;
        System.out.println("\n Displaying Payments \n");
        LedgerApp.displayTransactions(false); // Filter amounts less than 0
        for(Transactions transactions: LedgerApp.transactions){
            totalPay += transactions.amount;
        }

        System.out.println("Total: " + totalPay);
    }

}

