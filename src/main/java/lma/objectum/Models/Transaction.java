package lma.objectum.Models;

import java.sql.Date;

public class Transaction {

    private int id;
    private int userId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;
    private String status;
    private double fine;

    /**
     * Transaction constructor.
     *
     * @param id id
     * @param userId userId
     * @param bookId bookId
     * @param borrowDate borrowDate
     * @param returnDate returnDate
     * @param status status
     * @param fine fine
     */
    public Transaction(int id, int userId, int bookId, Date borrowDate, Date returnDate, String status, double fine) {

        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
