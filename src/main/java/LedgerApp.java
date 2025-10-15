import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> transactions = new ArrayList<>();

    public static void main(String[] args) {
        loadTransaction();
        displayHomeMenu();
    }

    private static void displayHomeMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n --- Home Screen --- \n");
            System.out.println("L) View Ledger");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("X) Exit");
            System.out.println("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice.toUpperCase()) {
                case "L":
                    System.out.println(" View Ledger");
                    break;
                case "D":
                    System.out.println(" Add Deposit ");
                    break;
                case "P":
                    System.out.println(" Make Payment");
                    break;
                case "X":
                    running = false;
                    System.out.println("Exiting Application");
                    break;
                default:
                    System.out.println("Invalid choice! Please choose one of the following options: L, D, P, or X ");
            }
        }
    }

    private static void loadTransaction() {

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine())  != null) {
                String[] actions = line.split("\\|");
                if (actions.length == 4 ) {
                    String dateTimeString = actions[0].trim();
                    LocalDate date = LocalDate.parse(actions[0]);
                    LocalTime time = LocalTime.parse(actions[1]);
                    String description = actions[2];
                    String vendor = actions[3];
                    double amount = Double.parseDouble(actions[3]);
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

            String line = String.format("%s %s | %s | %s | %.2f",
                    transactions.getDate(),
                    transactions.getTime(),
                    transactions.getDescription(),
                    transactions.getVendor(),
                    transactions.getAmount()
            );
            printWriter.println();

        } catch (IOException e) {
            System.out.println("Error saving transaction to file: " + e);
        }
    }
}

