package patel.mohawk.capstoneproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import patel.mohawk.capstoneproject.EditActivity;
import patel.mohawk.capstoneproject.R;

public class ProfileFragment extends Fragment {
    String TAG ="profile";
    private ProfileViewModel profileViewModel;
    FirebaseAuth auth;
    FirebaseUser user;
    View mroot;
    String data ="";
    Button editProfile;
    Map<String,Object> rentMovies = new HashMap<>();
    Map<String,Object> favMovies = new HashMap<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = root.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(view -> {
            editProfile();
        });
        mroot = root;
        runOnStart();

        return root;

    }



    private void editProfile(){
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Intent intent = new Intent(mroot.getContext(), EditActivity.class);
        intent.putExtra("uid",user.getUid());
        startActivity(intent);
    }

    private void runOnStart(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        TextView userName = mroot.findViewById(R.id.userName);
        TextView userEmail = mroot.findViewById(R.id.userEmailForProfile);
        Log.d("log3",user.getUid()+"   "+user.getDisplayName());
        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        DocumentReference docRef = db.collection(user.getUid()).document("Rent");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        rentMovies = document.getData();

                        addToString(false,rentMovies.size(),"Rented");
                        Log.d(TAG, "DocumentSnapshot data: " + rentMovies.size()+"");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        DocumentReference docRef1 = db.collection(user.getUid()).document("Fav");
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        favMovies = document.getData();
                        addToString(true,favMovies.size(),"Favourite");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });


        addDataToRecylerView(rentMovies,favMovies);
    }

    private void addDataToRecylerView(Map<String, Object> rentMovies, Map<String, Object> favMovies) {

    }

    private void addToString(boolean b, int size,String from){

        TextView stats = mroot.findViewById(R.id.userStats);

            data += from+" Movies Count : "+size+"\n \n";

           if(b){
               stats.setText(data);
           }


    }
}