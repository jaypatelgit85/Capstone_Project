package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.userEmail)
    EditText userEmail;
    @BindView(R.id.userPassword)
    EditText userPassword;
    @BindView(R.id.btn_signin)
    Button signIn;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);
        signIn.setEnabled(false);
        listenerAdditions();

    }

    /**
     * add listeneners to teh email and password field to check validity
     */
    private void listenerAdditions() {
        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                    signIn.setEnabled(true);
                }else{
                    signIn.setEnabled(false);
                }

            }
        });

    }

    /**
     * calls forgot password acticity
     * @param view
     */
    public void forgot_password(View view) {
        Intent intent=new Intent(this, ForgotPassword.class);
        startActivity(intent);


    }

    /**
     * calls sign up activity
     * @param view
     */
    public void sign_up(View view) {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }


    /**
     * authenticates the credentials and calls a function to check if admin,employee,user is logging in
     * authenticates the credentials and calls a function to check if admin,employee,user is logging in
     * @param view
     */
    public void login(View view) {
        auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In", "signInWithEmail:success");
                            user = auth.getCurrentUser();

                                Toast.makeText(MainActivity.this, "Sign In Succesful", Toast.LENGTH_SHORT).show();

                                    callOtherFunction();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign In", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    /**
     * is called once credentials are correct to see if user or admin or employee is logging in
     */
    private void callOtherFunction() {
        Log.d("In","in Other Function");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("Please","in else");
        DocumentReference docRef = db.collection("isEmployee").document(userEmail.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Please","inDocument found");
                        goToEmployeePage();
                    } else {
                        Log.d("Please","in document not found");
                        if(userEmail.getText().toString().equals("085.jay@gmail.com")){
                            gotToAdminHome();
                        }else{
                            homePage();
                        }

                    }
                } else {

                }
            }
        });


    }

    private void goToEmployeePage() {
        Intent intent = new Intent(this, EmployeeHome.class);
        startActivity(intent);
    }

    private void gotToAdminHome() {
        Intent intent = new Intent(this,AdminHome.class);
        startActivity(intent);
    }

    public  void homePage(){
        Intent intent = new Intent(this,HomePage.class);
        startActivity(intent);
    }

    public void goToHomePage(View view){

        Intent intent = new Intent(this,AdminHome.class);
        startActivity(intent);

    }



    public void abc(View view) {
        Intent intent = new Intent(this,HomePage.class);
        startActivity(intent);

    }
}

