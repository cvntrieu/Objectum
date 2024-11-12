package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class IsbnSearchStrategy implements SearchStrategy {
    @Override
    public boolean match(Book book, String keyword) {
        return book.getIsbn().toLowerCase().contains(keyword.toLowerCase());
    }
}
