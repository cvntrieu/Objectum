package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class DateSearchStrategy implements SearchStrategy {

    /**
     * Does the keyword match any books.
     *
     * @param book book
     * @param keyword to search
     *
     * @return is matched
     */
    @Override
    public boolean match(Book book, String keyword) {
        return book.getDate().toLowerCase().contains(keyword.toLowerCase());
    }
}