package lma.objectum.Utils;
import java.sql.Date;

public interface FineStrategy {
    double calculateFine(Date returnDate, Date dueDate);
}

