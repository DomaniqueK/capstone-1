import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {

    LocalDate date;
    LocalTime time;
    String description;
    String type;
    double amount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Transactions(LocalDate date, LocalTime time, String description, String type, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.type = type;
        this.amount = amount;

    }
}
