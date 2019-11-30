package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Reviews extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    String movieId;
    Intent intent;
    String TAG ="Reviews";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
                FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        intent = getIntent();
        movieId = intent.getStringExtra("uid");
        getReviews();
    }

    /**
     * sets up teh view for teh reviews
     * @param reviews
     */
    private void initRecyleerView(ArrayList<String> reviews){




        RecyclerView recyclerView = findViewById(R.id.allReviews);

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * is called once the actiity is loading in order to get all teh reviews of the movie sleeted and display it
     */
    private void getReviews() {
        ArrayList<String> reviews= new ArrayList<>();
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("AllReviews").document(movieId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        for(String key: document.getData().keySet()){

                            reviews.add(document.getData().get(key).toString());

                        }


                        initRecyleerView(reviews);

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
     * adds a rview to teh database as well as to the view and sends a notification as well
     * @param view
     */
    public void addReview(View view) {
        EditText text = findViewById(R.id.userReview);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> data = new HashMap<>();


        data.put(user.getUid(),text.getText().toString());

        db.collection(user.getUid()).document("Reviews")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        db.collection("AllReviews").document(movieId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });
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
                .setContentTitle("Review")
                .setContentText("You Just Added a new review")
                .setContentInfo("");
        notificationManager.notify(1, notificationBuilder.build());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();    //Do something after 100ms
            }
        }, 3000);



    }
}
