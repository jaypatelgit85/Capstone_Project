package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_movie);
    }

    public void addNewMovieToDatabase(View view) {
        EditText movieName = findViewById(R.id.movieName);
        EditText moviePlot = findViewById(R.id.plot);
        EditText movieAwards = findViewById(R.id.awards);
        EditText movieRatings = findViewById(R.id.ratings);
        String id = UUID.randomUUID().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> data = new HashMap<>();


        data.put("id",id);
        data.put("name",movieName.getText().toString());
        data.put("plot",moviePlot.getText().toString());
        data.put("awards",movieAwards.getText().toString());
        data.put("ratings",movieRatings.getText().toString());

        db.collection("Added Movies").document(id)
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


        finish();

    }
}
