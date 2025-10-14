import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    Scanner scanner = new Scanner(System.in);
    ArrayList<Transactions> transactions = new ArrayList<>();

    public static void main(String[] args) {
    }

    public static void loadtransaction(String[] args) {

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        } catch (IOException e) {
            System.out.println("Please select another option " + e);
        }
    }
    }
