package lma.objectum.Utils;

import lma.objectum.Models.Book;

public interface SearchStrategy {

    /**
     * Does the keyword match any book.
     *
     * @param book book
     * @param keyword to search
     *
     * @return true or false
     */
    boolean match(Book book, String keyword);
}
