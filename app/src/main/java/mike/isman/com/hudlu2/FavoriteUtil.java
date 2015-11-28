package mike.isman.com.hudlu2;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import mike.isman.com.hudlu2.models.Favorite;
import mike.isman.com.hudlu2.models.MashableNewsItem;

/**
 * Created by mike on 11/26/15.
 */
public class FavoriteUtil {
    public static void addFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        Favorite favorite = realm.createObject(Favorite.class); // Create a new object
        favorite.setAuthor(newsItem.author);
        favorite.setImage(newsItem.image);
        favorite.setLink(newsItem.link);
        favorite.setTitle(newsItem.title);
        realm.commitTransaction();
    }

    public static void removeFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        RealmResults<Favorite> result = realm.where(Favorite.class)
                .equalTo("link", newsItem.link)
                .findAll();

        result.clear();
        realm.commitTransaction();
    }

    public static boolean isFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);

        RealmResults<Favorite> result = realm.where(Favorite.class)
                .equalTo("link", newsItem.link)
                .findAll();

        return result.size() > 0;
    }

    public static RealmResults<Favorite> getAllFavorites(Context context) {
        Realm realm = Realm.getInstance(context);

        return realm.where(Favorite.class).findAll();
    }
}
