package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageUsers extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    String TAG="Manage Users";
    ArrayList<String> uid = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> emails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        callMainMethod();
    }
    /**
     * is called when teh activity is loading
     * gets the dtaa of every users and sends it tp userAdapter to show to the admin
     */
    private void callMainMethod() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AllUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uid.add(document.getId());
                                names.add(document.getString("fullName"));
                                emails.add(document.getString("email"));

                            }
                            initRecyleerView();
                            Log.d(TAG,"fgjvfgfcf");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    /**
     * sets up the view for teh admin to see teh users
     */
    private void initRecyleerView(){
        Log.d(TAG,"in init");
        RecyclerView recyclerView = findViewById(R.id.usersListRecyclerView);
        UsersAdapter recyclerViewAdapter = new UsersAdapter(uid,names,emails,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
