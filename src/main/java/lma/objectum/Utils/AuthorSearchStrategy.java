package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public boolean match(Book book, String keyword) {
        return book.getAuthors().toLowerCase().contains(keyword.toLowerCase());
    }
}
