import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class LedgerApp {
    // Declare and instantiate the Scanner and ArrayList so that it can be used throughout entire application
    public static final Scanner scanner = new Scanner(System.in);
    public static final ArrayList<Transactions> transactions = new ArrayList<>();
    String transactionFile = "transactions.csv";
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    static double balance = 0;




    public static void loadTransaction() {
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




    static void saveTransaction(Transactions transactions) {
        String transactionFile = "transactions.csv"; // Use FileWriter when true to append the existing csv file
        try (FileWriter fileWriter = new FileWriter(transactionFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter)
        ) {

            String line = String.format("%s|%s|%s|%s|%.2f", // Format to match csv file with pipes to separate
                    transactions.getDate(),
                    transactions.getTime().format(formatter),
                    transactions.getDescription(),
                    transactions.getVendor(),
                    transactions.getAmount()
            );
            printWriter.println(line); // Writes the new line to the csv file
        } catch (IOException e) { // catch file writing  errors
            System.out.println("Error saving transaction to file: " + e);
        }
    }

    public static void displayTransactions(boolean isDeposit) {
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
        System.out.printf("%-12s | %-10s | %20s | %15s | %s\n", // Print header so it aligns with file format
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("  ");
        for (Transactions t : filteredList) {
            System.out.printf("%-12s | %-10s | %-25s | %15s | %.2f\n", // Print list items
                    t.getDate(),
                    t.getTime().format(formatter),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        if (filteredList.isEmpty()) { // Displays message if no results are found
            System.out.println("No matching transactions found.");
        }
    }


}

