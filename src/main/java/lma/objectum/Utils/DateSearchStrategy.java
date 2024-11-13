package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class DateSearchStrategy implements SearchStrategy {
    @Override
    public boolean match(Book book, String keyword) {
        return book.getDate().toLowerCase().contains(keyword.toLowerCase());
    }
}