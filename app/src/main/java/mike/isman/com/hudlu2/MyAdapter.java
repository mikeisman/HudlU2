package mike.isman.com.hudlu2;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.List;

import mike.isman.com.hudlu2.models.MashableNewsItem;

/**
 * Created by mike on 11/14/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MashableNewsItem> mDataset;
    public OnAdapterInteractionListener mListener;
    public RequestQueue mRequestQueue;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView authorTextView;
        public ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.item_title);
            authorTextView = (TextView) v.findViewById(R.id.item_author);
            imageView = (ImageView) v.findViewById(R.id.item_image);
        }
    }

    public interface OnAdapterInteractionListener {
        void onItemClicked(View view, int position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<MashableNewsItem> myDataset) {
        mDataset = myDataset;
        mListener = (OnAdapterInteractionListener)context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class4, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MashableNewsItem newsItem = mDataset.get(position);
        holder.titleTextView.setText(newsItem.title);
        holder.authorTextView.setText(newsItem.author);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(v, position);
            }
        });
        final ViewHolder finalHolder = holder;
        ImageRequest imageRequest = new ImageRequest(newsItem.image, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                finalHolder.imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Hudl U", volleyError.getMessage());
            }
        });
        mRequestQueue.add(imageRequest);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}