import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transactions> transactions = new ArrayList<>();

    public static void main(String[] args) {
        displayHomeMenu();
    }

    private static void displayHomeMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n --- Home Screen --- \n");
            System.out.println("L) Ledger View");
            System.out.println("A) Add Payment/Deposit");
            System.out.println("D) Reports");
            System.out.println("X) Exit");
            System.out.println("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "L":
                    System.out.println(" Loading Ledger View ");
                    break;
                case "A":
                    System.out.println(" Loading Add Transactions ");
                    break;
                case "D":
                    System.out.println(" Loading Reports ");
                    break;
                case "X":
                    running = false;
                    System.out.println("Exiting Application");
                    break;
                default:
                    System.out.println("Invalid choice! Please choose one of the following options: L, A, D, or X ");
            }
        }
    }

    public static void loadTransaction(String[] args) {

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();

            String input = "";
            boolean running = true;
            while (running= (bufferedReader.readLine())  != null) {
                String[] actions = input.split("\\|");
                if (actions.length >= 5 ) {
                    LocalDate date = LocalDate.parse(actions[0]);
                    LocalTime time = LocalTime.parse(actions[1]);
                    String description = actions[2];
                    String type = actions[3];
                    double amount = Double.parseDouble(actions[4]);
                    Transactions newTransaction = new Transactions(date, time, description, type, amount);
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
        return prompt;
    }

}

