import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Transactions {

    // Steps
    // Create transactions.csv file
    // Open file in the bufferedreader
    // Read the header
    // Break it up in parts
    // Use parts to instantiate ledger object??


    public static void main(String[] args) {

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e);
        }
    }
}
