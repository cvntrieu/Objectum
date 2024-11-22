package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class IsbnSearchStrategy implements SearchStrategy {

    /**
     * Does the keyword match any book.
     *
     * @param book book
     * @param keyword to search
     *
     * @return is match or not
     */
    @Override
    public boolean match(Book book, String keyword) {
        return book.getIsbn().toLowerCase().contains(keyword.toLowerCase());
    }
}
