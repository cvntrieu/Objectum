package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class AuthorSearchStrategy implements SearchStrategy {

    /**
     * Does the keyword match any book.
     *
     * @param book book
     * @param keyword to search
     *
     * @return is keyword matched
     */
    @Override
    public boolean match(Book book, String keyword) {
        return book.getAuthors().toLowerCase().contains(keyword.toLowerCase());
    }
}
