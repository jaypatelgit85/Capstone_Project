package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShowMovie extends AppCompatActivity {
    JSONArray results;
    String TAG ="ShowMovie";
    int position;
    private RequestQueue requestQueue;
    FirebaseAuth auth;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        requestQueue = Volley.newRequestQueue(getBaseContext());
        Intent intent=getIntent();
        try {
            results = new JSONArray(intent.getStringExtra("jsonArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        position = intent.getIntExtra("position",0);

        try {
            primaryFunction();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_show_movie);



    }

    /**
     * gets the details of the movie selected and also downloads the image for the movie
     * @throws JSONException
     */
    private void primaryFunction() throws JSONException {
        String url = "http://www.omdbapi.com/?i="+results.getJSONObject(position).getString("imdbID")+"&apikey=28f258f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    new DownloadImageTask().execute(response.getString("Poster"));
                    TextView movieName = findViewById(R.id.movieShowName);
                    movieName.setText(response.get("Title")+"");

                    JSONArray jsonArrayRatings = response.getJSONArray("Ratings");
                    String ratings="";
                    for(int i =0;i<jsonArrayRatings.length();i++){
                        ratings += jsonArrayRatings.getJSONObject(i).getString("Source")+" : - "+jsonArrayRatings.getJSONObject(i).getString("Value")+"\n";
                    }
                    ratings +="Meta Score: "+response.getString("Metascore")+"\nIMDB Rating :"+response.getString("imdbRating");
                    TextView movieRatings = findViewById(R.id.movieRatings);
                    movieRatings.setText(ratings);
                    TextView movieDetails = findViewById(R.id.movieShowDetails);
                    movieDetails.setText("Plot : "+response.get("Plot")+"\n \n Awards : "+response.getString("Awards"));
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

    /**
     * adds the movie to the rent database and turn the button green , also gives the notification
     * @param view
     * @throws JSONException
     */

    public void rentMovie(View view) throws JSONException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(TAG,user.getUid()+"");
        Button button = findViewById(R.id.rent);
        button.setBackgroundColor(Color.GREEN);

        Map<String,String> data = new HashMap<>();
        data.put(results.getJSONObject(position).getString("imdbID"),results.getJSONObject(position).getString("imdbID"));
        db.collection(user.getUid()).document("Rent")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        try {
                            startNotification("Rent");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });




    }


    /**
     * adds the movie to the fav database and turn the button green , also gives the notification
     * @param view
     * @throws JSONException
     */

    public void addToFav(View view) throws JSONException {
        Button button = findViewById(R.id.fav);
        button.setBackgroundColor(Color.GREEN);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(TAG,user.getUid()+"");
        Map<String,String> data = new HashMap<>();
        data.put(results.getJSONObject(position).getString("imdbID"),results.getJSONObject(position).getString("imdbID"));
        db.collection(user.getUid()).document("Fav")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        try {
                            startNotification("Fav");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    /**
     * builds a notification for the user with some details and displays it
     * @param where
     * @throws JSONException
     */
    public void startNotification(String where) throws JSONException {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Jay";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Movie");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Movie App")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("New Movie added to "+ where)
                .setContentText(results.getJSONObject(position).get("Title").toString()+" was just added to "+where)
                .setContentInfo("");
        notificationManager.notify(1, notificationBuilder.build());
    }




    public void goToReviews(View view) throws JSONException {
        Intent intent = new Intent(this,Reviews.class);
        intent.putExtra("uid",results.getJSONObject(position).getString("imdbID"));
        startActivity(intent);
    }

    /**
     * download image for the moviex=
     */

    private class DownloadImageTask extends AsyncTask<String , Void, Bitmap> {


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
                ImageView moviePoster = findViewById(R.id.movieShowPoster);
                moviePoster.setImageBitmap(result);


            }
        }

    }



}
