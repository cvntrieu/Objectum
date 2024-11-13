package lma.objectum.Utils;

import lma.objectum.Models.Book;

public class SearchContext {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean executeSearch(Book book, String keyword) {
        return strategy.match(book, keyword);
    }
}
