
package lma.objectum.Models;

public class FinedBook {

    private String title;
    private double fine;

    /**
     * Fined book.
     *
     * @param title title
     * @param fine fine
     */
    public FinedBook(String title, double fine) {

        this.title = title;
        this.fine = fine;
    }

    public String getTitle() {
        return title;
    }

    public double getFine() {
        return fine;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
