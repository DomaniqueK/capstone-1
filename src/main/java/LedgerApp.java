import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
  public static ArrayList<Transactions> transactions = new ArrayList<>();
  public static Scanner scanner = new Scanner(System.in);

    // Steps
    // Create transactions.csv file
    // Open file in the bufferedreader
    // Read the header
    // Break it up in parts
    // Use parts to instantiate ledger object??

    public static void main(String[] args) {
        loadtransactions();
        displayHomeMenu();
    }

        public static  void loadtransactions() {
            try {
                FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] actions = line.split("\\|");
                    if (actions.length >= 5) {
                        LocalDate date = LocalDate.parse(actions[0].trim());
                        LocalTime time = LocalTime.parse(actions[1].trim());
                        String description = actions[2].trim();
                        String type = actions[3].trim();
                        double amount = Double.parseDouble(actions[4].trim());
                        Transactions newTransaction = new Transactions(date, time, description, type, amount);
                        transactions.add(newTransaction);
                    }

                }

            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e);
            } catch (IOException e) {
                System.out.println("Error reading file: " + e);
            }
        }

    public static void displayHomeMenu() {
        boolean running = true;
        while (running) {
                System.out.println("\n --- Home Screen --- \n");
                System.out.println("L) Ledger View");
                System.out.println("A) Add Payment/Deposit");
                System.out.println("D) Reports");
                System.out.println(" X) Exit");
                System.out.println("Enter your choice: ");

                String choice = scanner.nextLine();
        }
        }
    }

