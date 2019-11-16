package patel.mohawk.capstoneproject.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import patel.mohawk.capstoneproject.HomePage;
import patel.mohawk.capstoneproject.R;
import patel.mohawk.capstoneproject.RecyclerViewAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Bitmap> mImages = new ArrayList<>();
    private RequestQueue requestQueue;
    Button searchButton;
    View intialRoot;
    String[] identifiers;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        intialRoot = root;
//        initBitmapImages();
        requestQueue = Volley.newRequestQueue(root.getContext());


        searchButton = root.findViewById(R.id.serachForMovie);
// Register the onClick listener with the implementation above

        searchButton.setOnClickListener(view -> {
            mainFunction();
        });

        return root;
    }



    private void mainFunction(){

            EditText userQuery = intialRoot.findViewById(R.id.userSearch);
            String userQueryString = userQuery.getText().toString();
            String url = "http://www.omdbapi.com/?s="+userQueryString+"&apikey=28f258f4";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("Search");
                        initBitmapImagesAndNames(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });


            requestQueue.add(jsonObjectRequest);

    }

    private void initBitmapImagesAndNames(JSONArray jsonArray) throws JSONException {
        identifiers = new String[jsonArray.length()];
        mNames.clear();
        mImages.clear();
        String[] movies = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++ ){
            mNames.add(jsonArray.getJSONObject(i).getString("Title"));
            identifiers[i] = jsonArray.getJSONObject(i).getString("imdbID");
            movies[i] = jsonArray.getJSONObject(i).getString("Poster");

        }


        new DownloadImageTask().execute(movies);
//            initRecyleerView();


    }

    private void initRecyleerView(){

        RecyclerView recyclerView = intialRoot.findViewById(R.id.moviesListRecyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mNames,mImages,intialRoot.getContext());

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(intialRoot.getContext()));

    }

    private class DownloadImageTask extends AsyncTask<String , Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bmp = null;

            try {
                URL url = null;
                for(int i =0; i<strings.length;i++){
                    url = new URL(strings[i]);

                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    mImages.add(bmp);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Find the ImageView object to use


                    initRecyleerView();


            }
        }

    }


}