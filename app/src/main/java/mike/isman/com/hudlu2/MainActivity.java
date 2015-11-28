package mike.isman.com.hudlu2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mike.isman.com.hudlu2.models.MashableNews;
import mike.isman.com.hudlu2.models.MashableNewsItem;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnAdapterInteractionListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MashableNewsItem> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fetchLatestNews();

        showWelcomeDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            Log.d("HudlU", "Favorites menu item clicked.");
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position) {
        //Snackbar.make(view, myDataset.get(position).author, Snackbar.LENGTH_SHORT).show();
        MashableNewsItem mashableNewsItem = myDataset.get(position);
        Uri webpage = Uri.parse(mashableNewsItem.link);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onFavoriteClicked(View view, int position) {
        MashableNewsItem mashableNewsItem = myDataset.get(position);
        boolean isFavorited = FavoriteUtil.isFavorite(this, mashableNewsItem);
        if(isFavorited) {
            FavoriteUtil.removeFavorite(this, mashableNewsItem);
        } else {
            FavoriteUtil.addFavorite(this, mashableNewsItem);
        }
    }

    public void fetchLatestNews() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            Toast.makeText(getApplicationContext(), "Fetching latest news", Toast.LENGTH_SHORT).show();

            StringRequest request = new StringRequest(Request.Method.GET,
                    "http://mashable.com/stories.json?hot_per_page=0&new_per_page=5&rising_per_page=0",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            MashableNews mashableNews = new Gson().fromJson(response, MashableNews.class);
                            Log.d("HudlU", mashableNews.newsItems.get(0).title);
                            myDataset.addAll(mashableNews.newsItems);
                            mAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "An error occurred getting the latest news", Toast.LENGTH_SHORT).show();
                            Log.d("HudlU", error.getMessage());
                        }
                    });
            requestQueue.add(request);
        } else {
            Toast.makeText(getApplicationContext(), "You are not connected to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void showWelcomeDialog() {
        SharedPreferences preferences = getSharedPreferences("HudlUPrefs", Context.MODE_PRIVATE);
        boolean firstRun = preferences.getBoolean("firstRun", true);

        if (firstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.welcome_dialog_message)
                    .setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void clickedFavorite(View view) {
        RelativeLayout viewParentRow = (RelativeLayout)view.getParent();
        /*

        final int position = mRecyclerView.getChildLayoutPosition(viewParentRow);
        MashableNewsItem mashableNewsItem = myDataset.get(position);

        Button button = (Button)viewParentRow.getChildAt(1);

        Button button = (Button)view;
        final int position = mRecyclerView.getChildLayoutPosition(button);
        MashableNewsItem mashableNewsItem = myDataset.get(position);


        Log.d("Hudl U", "I clicked " + mashableNewsItem.title);
        button.setText("Favorited");

        viewParentRow.refreshDrawableState();
        */
    }

}
