package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Intent intent;
    EditText name;
    TextView email;
    EditText number;
    String TAG = "Edit_Profile";

    EditText userPass;

    String uid;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userPass = findViewById(R.id.profilePassword);
        intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = findViewById(R.id.profileFullName);
        email = findViewById(R.id.profileEmail);
        number = findViewById(R.id.profilePhoneNumber);
        DocumentSnapshot doc;
        db = FirebaseFirestore.getInstance();

        /**
         * fill in teh data for teh user when calling edit profile for ease of use
         */
        DocumentReference docRef = db.collection(uid).document("userLogin");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> a = document.getData();
                        name.setText(a.get("fullName").toString());
                        number.setText(a.get("phoneNumber").toString());
                        email.setText(a.get("email").toString());
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
     * once button is clicked saves the changes to the profile and send teh data back to database
     * @param view
     */
    public void editUserProfile(View view) {

        Map<String, Object> data = new HashMap<>();
        data.put("fullName",name.getText().toString());
        data.put("phoneNumber",number.getText().toString());
        Log.d(TAG,uid);
        db.collection(uid).document("userLogin")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "User profile updated.");
                        }
                    }
                });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();    //Do something after 100ms
            }
        }, 2000);



    }


    /**
     * deletes teh profile of teh user permanently
     * @param view
     */
    public void deleteUserProfile(View view) {
    user= mAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            startMain();

                        }
                    }
                });
    }

    /**
     * starts main activity again
     */
    public void startMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
