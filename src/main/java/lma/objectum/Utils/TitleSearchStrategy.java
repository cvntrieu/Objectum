package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class TitleSearchStrategy implements SearchStrategy {

    /**
     * Does the keyword match any book's attribute.
     *
     * @param book book
     * @param keyword searched keyword
     *
     * @return true or false
     */
    @Override
    public boolean match(Book book, String keyword) {
        return book.getTitle().toLowerCase().contains(keyword.toLowerCase());
    }
}
