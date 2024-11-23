package lma.objectum.Models;

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

    // Constructor

    /**
     * Constructor.
     *
     * @param title title
     * @param authors authors
     * @param publisher publisher
     * @param publishedDate publishedDate
     * @param pageCount pageCount
     * @param categories categories
     * @param language language
     * @param averageRating averageRating
     * @param ratingsCount rating count
     * @param printType print type
     * @param previewLink preview link
     * @param description description
     */
    public BookInAPI(String title, String authors, String publisher, String publishedDate,
                     int pageCount, String categories, String language, double averageRating,
                     int ratingsCount, String printType, String previewLink, String description) {

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

    // Getter and Setter methods for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Overriding To-string.
     *
     * @return info of book
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

