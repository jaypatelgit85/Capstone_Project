package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShowMovieRent extends AppCompatActivity {
    TextView name;
    ImageView poster;
    String movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_rent);
        Intent intent = getIntent();
        String movie = intent.getStringExtra("movieName");
        movieId = intent.getStringExtra("id");
        name = findViewById(R.id.movieNameRent);
        poster = findViewById(R.id.moviePosterrent);
        name.setText(movie);
        byte[] byteArray = intent.getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        poster.setImageBitmap(bmp);

    }

    /**
     * removes the movie from teh rent database
     * @param view
     */
    public void removeRent(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(user.getUid()).document("Rent");

        // Remove the 'capital' field from the document
        Map<String,Object> updates = new HashMap<>();
        updates.put(movieId, FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
            // [START_EXCLUDE]
        });
        // [END update_delete_field]
        finish();

    }

}

