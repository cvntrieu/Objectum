package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public boolean match(Book book, String keyword) {
        return book.getTitle().toLowerCase().contains(keyword.toLowerCase());
    }
}
