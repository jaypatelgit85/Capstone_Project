package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RemoveEmployeeOrUser extends AppCompatActivity {

    String uid;
    String name;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_employee_or_user);
        TextView username =  findViewById(R.id.userNameToBeDeleted);
        TextView userEmail = findViewById(R.id.userEmailToBeDeleted);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        uid = intent.getStringExtra("uid");
        username.setText("User Name :- "+name);
        userEmail.setText("User Email :- "+email);

    }

    public void deleteUser(View view) {
       FirebaseAuth auth = FirebaseAuth.getInstance();

    }

    public void disableAccount(View view) {
    }
}
