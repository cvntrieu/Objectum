package lma.objectum.Utils;

import lma.objectum.Models.Book;

public interface SearchStrategy {
    boolean match(Book book, String keyword);
}
