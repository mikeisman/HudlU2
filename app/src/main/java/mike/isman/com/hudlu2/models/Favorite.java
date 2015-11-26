package mike.isman.com.hudlu2.models;

import io.realm.RealmObject;

/**
 * Created by mike on 11/26/15.
 */
public class Favorite extends RealmObject {
    public String title;
    public String author;
    public String link;
    public String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
