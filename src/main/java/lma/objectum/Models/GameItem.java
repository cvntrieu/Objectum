package lma.objectum.Models;

public class GameItem {
    private String title;
    private String imageUrl;

    public GameItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
