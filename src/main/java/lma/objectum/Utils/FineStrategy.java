package lma.objectum.Utils;
import java.sql.Date;

public interface FineStrategy {

    /**
     * Calculating fine.
     *
     * @param returnDate returned date
     * @param dueDate deadline
     *
     * @return fine
     */
    double calculateFine(Date returnDate, Date dueDate);
}

