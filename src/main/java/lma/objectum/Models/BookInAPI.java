package lma.objectum.Models;

/**
 * Represents a book fetched from an external API.
 * Contains information such as the title, authors, publisher, published date, page count, and more.
 */
public class BookInAPI {

    private String title;
    private String authors;
    private String publisher;
    private String publishedDate;
    private int pageCount;
    private String categories;
    private String language;
    private double averageRating;
    private int ratingsCount;
    private String printType;
    private String previewLink;
    private String description;
    private byte[] coverImage;

    /**
     * Constructor to create a BookInAPI instance with detailed information.
     *
     * @param title          The title of the book.
     * @param authors        The authors of the book.
     * @param publisher      The publisher of the book.
     * @param publishedDate  The date the book was published.
     * @param pageCount      The number of pages in the book.
     * @param categories     The categories associated with the book.
     * @param language       The language of the book.
     * @param averageRating  The average rating of the book.
     * @param ratingsCount   The number of ratings the book has received.
     * @param printType      The print type of the book.
     * @param previewLink    The link to preview the book.
     * @param description    The description of the book.
     */
    public BookInAPI(String title, String authors, String publisher, String publishedDate, int pageCount, String categories, String language, double averageRating, int ratingsCount, String printType, String previewLink, String description) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.categories = categories;
        this.language = language;
        this.averageRating = averageRating;
        this.ratingsCount = ratingsCount;
        this.printType = printType;
        this.previewLink = previewLink;
        this.description = description;
    }

    /**
     * Constructor to create a BookInAPI instance with basic information including cover image.
     *
     * @param title       The title of the book.
     * @param author      The author of the book.
     * @param publisher   The publisher of the book.
     * @param coverImage  The cover image of the book as a byte array.
     */
    public BookInAPI(String title, String author, String publisher, byte[] coverImage) {
        this.authors = author;
        this.title = title;
        this.publisher = publisher;
        this.coverImage = coverImage;
    }

    // Getter and Setter methods for all fields

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the authors of the book.
     *
     * @return The authors of the book.
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Sets the authors of the book.
     *
     * @param authors The authors to set.
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * Gets the publisher of the book.
     *
     * @return The publisher of the book.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of the book.
     *
     * @param publisher The publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the published date of the book.
     *
     * @return The published date of the book.
     */
    public String getPublishedDate() {
        return publishedDate;
    }

    /**
     * Sets the published date of the book.
     *
     * @param publishedDate The published date to set.
     */
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * Gets the page count of the book.
     *
     * @return The page count of the book.
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Sets the page count of the book.
     *
     * @param pageCount The page count to set.
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * Gets the categories associated with the book.
     *
     * @return The categories of the book.
     */
    public String getCategories() {
        return categories;
    }

    /**
     * Sets the categories of the book.
     *
     * @param categories The categories to set.
     */
    public void setCategories(String categories) {
        this.categories = categories;
    }

    /**
     * Gets the language of the book.
     *
     * @return The language of the book.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language of the book.
     *
     * @param language The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the average rating of the book.
     *
     * @return The average rating of the book.
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the average rating of the book.
     *
     * @param averageRating The average rating to set.
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Gets the number of ratings the book has received.
     *
     * @return The ratings count of the book.
     */
    public int getRatingsCount() {
        return ratingsCount;
    }

    /**
     * Sets the number of ratings for the book.
     *
     * @param ratingsCount The ratings count to set.
     */
    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    /**
     * Gets the print type of the book.
     *
     * @return The print type of the book.
     */
    public String getPrintType() {
        return printType;
    }

    /**
     * Sets the print type of the book.
     *
     * @param printType The print type to set.
     */
    public void setPrintType(String printType) {
        this.printType = printType;
    }

    /**
     * Gets the preview link of the book.
     *
     * @return The preview link of the book.
     */
    public String getPreviewLink() {
        return previewLink;
    }

    /**
     * Sets the preview link of the book.
     *
     * @param previewLink The preview link to set.
     */
    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    /**
     * Gets the description of the book.
     *
     * @return The description of the book.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the book.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the cover image of the book as a byte array.
     *
     * @return The cover image of the book.
     */
    public byte[] getCoverImage() {
        return coverImage;
    }

    /**
     * Sets the cover image of the book.
     *
     * @param coverImage The cover image to set.
     */
    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    /**
     * Returns a string representation of the book with all its details.
     *
     * @return A string with all the book's information.
     */
    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Authors: " + authors + "\n" +
                "Publisher: " + publisher + "\n" +
                "Published Date: " + publishedDate + "\n" +
                "Page Count: " + pageCount + "\n" +
                "Categories: " + categories + "\n" +
                "Language: " + language + "\n" +
                "Average Rating: " + averageRating + "\n" +
                "Ratings Count: " + ratingsCount + "\n" +
                "Print Type: " + printType + "\n" +
                "Preview Link: " + previewLink + "\n" +
                "Description: " + description;
    }
}
