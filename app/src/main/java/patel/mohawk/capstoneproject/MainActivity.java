package patel.mohawk.capstoneproject;

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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


    public void forgot_password(View view) {
        Intent intent=new Intent(this, ForgotPassword.class);
        startActivity(intent);


    }

    public void sign_up(View view) {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }

    public void login(View view) {
        auth=FirebaseAuth.getInstance();
        ;

        auth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In", "signInWithEmail:success");
                            user = auth.getCurrentUser();
                            if(user.isEmailVerified()){
                                Toast.makeText(MainActivity.this, "Sign In Succesful", Toast.LENGTH_SHORT).show();
                               // goToHomePage();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Email Not Verified Yet", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign In", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }


public void homepage(View view) {

//      DataVerification dataVerification=new DataVerification(abc);
//      dataVerification.first(abc);
    }

//    public void goToHomePage(){
//            Intent intent=new Intent(this,UserHomePage.class);
//            startActivity(intent);

   // }

}

