package mike.isman.com.hudlu2.models;

import java.util.List;

/**
 * Created by mike on 11/18/15.
 */
public class MashableNews {
    @SerializedName("new")
    public List<MashableNewsItem> newsItems;
}
