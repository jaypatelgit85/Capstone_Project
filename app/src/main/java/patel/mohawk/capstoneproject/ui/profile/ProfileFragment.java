package patel.mohawk.capstoneproject.ui.profile;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patel.mohawk.capstoneproject.EditActivity;
import patel.mohawk.capstoneproject.ProfileMovieAdapter;
import patel.mohawk.capstoneproject.R;
import patel.mohawk.capstoneproject.RecyclerViewAdapter;
import patel.mohawk.capstoneproject.ShowMovie;

public class ProfileFragment extends Fragment {
    String TAG = "profile";
    private ProfileViewModel profileViewModel;
    FirebaseAuth auth;
    FirebaseUser user;
    View mroot;
    String data = "";
    Button editProfile;
    JSONArray results;
    private RequestQueue requestQueue;
    int rentSize;
    int favSize;
    ArrayList<String> recylerViewRent = new ArrayList<>();
    ArrayList<String> recylerViewFav = new ArrayList<>();
    ArrayList<Bitmap> recylerViewRentMoviePoster = new ArrayList<>();
    ArrayList<Bitmap> recylerViewFavMoviePoster = new ArrayList<>();
    ArrayList<String> rentId = new ArrayList<>();
    ArrayList<String> favId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        requestQueue = Volley.newRequestQueue(root.getContext());
        editProfile = root.findViewById(R.id.editProfile);

        editProfile.setOnClickListener(view -> {
            editProfile();
        });
        mroot = root;
        runOnStart();
        return root;
    }

    /**
     * starts the edit profile activity if user wants to edit profile also send the uid in order to communicate with database
     *
     */
    private void editProfile() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = new Intent(mroot.getContext(), EditActivity.class);
        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }

    /**
     * calls the database and get the informatin needed like the rented mobvies and the fav movies also
     * count of the movies in order to display to teh user
     */
    private void runOnStart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        TextView userName = mroot.findViewById(R.id.userName);
        TextView userEmail = mroot.findViewById(R.id.userEmailForProfile);
        Log.d("log3", user.getUid() + "   " + user.getDisplayName());
        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());

        /**
         * get everything froom the rent collection
         */
        DocumentReference docRef = db.collection(user.getUid()).document("Rent");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        addToString(false, document.getData().size(), "Rented");
                        rentSize = document.getData().size();
                        for (String key : document.getData().keySet()) {
                            rentId.add(key);
                            getData(key,"rent");
                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().size() + "");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });
        /**
         * get everything from the favourite collection
         */

        DocumentReference docRef1 = db.collection(user.getUid()).document("Fav");
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        favSize = document.getData().size();
                        addToString(true, document.getData().size(), "Favourite");
                        for (String key : document.getData().keySet()) {
                            favId.add(key);
                            getData(key,"fav");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });

    }

    /**
     * get the movie info based upon the user database
     * @param key
     * @param method
     */
    private void getData(String key, String method) {
        String url = "http://www.omdbapi.com/?i=" + key + "&apikey=28f258f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (method.equals("rent")) {
                        new DownloadImageTask().execute(response.getString("Poster"));
                        recylerViewRent.add(response.get("Title").toString());
                    } else {

                        new DownloadImageTask1().execute(response.getString("Poster"));
                        recylerViewFav.add(response.get("Title").toString());

                    }


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


    private void addToString(boolean b, int size, String from) {

        TextView stats = mroot.findViewById(R.id.userStats);

        data += from + " Movies Count : " + size + "\n \n";

        if (b) {
            stats.setText(data);
        }


    }

    /**
     * sets up the view for rented movies
     */

    private void initRecyleerView(){

        RecyclerView recyclerView = mroot.findViewById(R.id.rentRecylerView);
        ProfileMovieAdapter recyclerViewAdapter = new ProfileMovieAdapter(recylerViewRent,recylerViewRentMoviePoster,rentId,"rent",mroot.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mroot.getContext(),LinearLayoutManager.HORIZONTAL,false));

    }

    /**
     * sets up the view for fav movies
     */
    private void initRecyleerView1(){

        RecyclerView recyclerView = mroot.findViewById(R.id.favRecylerView);
        ProfileMovieAdapter recyclerViewAdapter = new ProfileMovieAdapter(recylerViewFav,recylerViewFavMoviePoster,favId,"fav",mroot.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mroot.getContext(),LinearLayoutManager.HORIZONTAL,false));

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bmp = null;

            try {
                URL url = null;

                url = new URL(strings[0]);

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Find the ImageView object to use
                recylerViewRentMoviePoster.add(result);
                if(recylerViewRent.size()==rentSize && recylerViewRent.size()==recylerViewRentMoviePoster.size()){
                    initRecyleerView();

                }


            }
        }
    }

    /**
     * donwload poster for the movie
     */
    private class DownloadImageTask1 extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bmp = null;

            try {
                URL url = null;

                url = new URL(strings[0]);

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Find the ImageView object to use
                recylerViewFavMoviePoster.add(result);
                if(recylerViewFav.size()==favSize && recylerViewFav.size()==recylerViewFavMoviePoster.size()){
                    initRecyleerView1();
                }
            }
        }
    }

}