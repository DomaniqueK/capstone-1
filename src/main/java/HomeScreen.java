import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeScreen {

    static double balance = 0;

    public static void displayHomeMenu() {
        // Using a while loop to make sure the display menu runs until the users chooses to exit
        boolean running = true;
        while (running) { // Print out the Home menu options
            System.out.println("\n --- Home Screen --- \n");
            System.out.println("L) View Ledger");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            String choice = LedgerApp.scanner.nextLine().toUpperCase();
            // Use a switch statement to route the users input to the correct sub-method they want and have ability to switch through the screens
            switch (choice) {
                case "L" -> LedgerScreen.displayLedgerScreen(); // Call the ledger
                case "D" -> HomeScreen.addDeposit(); // Call to make deposit method and updates the application/ledger
                case "P" -> HomeScreen.makePayment(); // Call to make payment method and updates the application/ledger
                case "X" -> {
                    running = false;
                    // Exit the program
                    System.out.println("Exiting Application");
                }
                default -> System.out.println("Invalid choice! Please choose one of the following options: L, D, P, or X "); // Used for invalid choices
            }
        }
    }

    public static void makePayment() {
        System.out.println("\n Make Payment ");
        String description = LedgerApp.getInput(LedgerApp.scanner, "Enter description: ");
        String vendor = LedgerApp.getInput(LedgerApp.scanner, "Enter vendor/source: ");
        double amountInput = Double.parseDouble(LedgerApp.getInput(LedgerApp.scanner, "Enter amount")); // Get positive amount from input
        double amount = -amountInput; // convert the positive amount to a negative amount for payments/debits
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount); // Create new transaction with a negative amount (payment)
        LedgerApp.transactions.add(newTransaction);  // Add the new transaction to the list
        LedgerApp.saveTransaction(newTransaction);
        balance += newTransaction.amount;
        System.out.println("Current balance: " + balance);// Put the new transaction on the csv file
        System.out.println("Payment successfully made! Just lay down until you get paid.");
    }

    public static void addDeposit() {
        System.out.println("\n Add Deposit ");
        String description = LedgerApp.getInput(LedgerApp.scanner, "Enter description: ");
        String vendor = LedgerApp.getInput(LedgerApp.scanner, "Enter vendor/source: ");
        double amount = Double.parseDouble(LedgerApp.getInput(LedgerApp.scanner, "Enter amount"));
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Transactions newTransaction = new Transactions(date, time, description, vendor, amount); // Create new transaction with a positive amount (deposit)
        LedgerApp.transactions.add(newTransaction); // Add the new transaction to the list
        LedgerApp.saveTransaction(newTransaction); // Put the new transaction on the csv file

        balance += newTransaction.amount;
        System.out.println("Current balance: " + balance);
        System.out.println("Deposit made! Get a sweet treat!");
    }
}
