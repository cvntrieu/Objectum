package lma.objectum.Utils;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

public class BasicFine implements FineStrategy {

    private static final double DAILY_FINE = 1.0;

    /**
     * Calculating fine.
     *
     * @param returnDate returned date
     * @param dueDate deadline
     *
     * @return fine
     */
    @Override
    public double calculateFine(Date returnDate, Date dueDate) {
        long diffInMillis = dueDate.getTime() - returnDate.getTime();
        long overdueDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        return overdueDays > 0 ? overdueDays * DAILY_FINE : 0.0;
    }
}

