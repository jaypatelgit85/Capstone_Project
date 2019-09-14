package patel.mohawk.capstoneproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity {
    @BindView(R.id.inputFullName)
    EditText userFullName;
    @BindView(R.id.inputPhoneNumber)
    EditText phoneNumber;
    @BindView(R.id.inputEmail)
    EditText userEmail;
    @BindView(R.id.inputPassword)
    EditText userPassword;
    @BindView(R.id.inputSecurityQuestionAnswer)
    EditText userSecurityQuestionAnswer;
    @BindView(R.id.inputSecurityQuestion)
    EditText userSecurityQuestion;
    @BindView(R.id.btn_signup)
    Button signUp;
    ArrayList<EditText> temp;
    FirebaseAuth mAuth;
    DataVerification dataVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        temp = new ArrayList<>();
        temp.add(userFullName);
        temp.add(userEmail);
        temp.add(userPassword);
        temp.add(userSecurityQuestion);
        temp.add(userSecurityQuestionAnswer);
        temp.add(phoneNumber);
        signUp.setEnabled(true);
        dataVerification = new DataVerification(temp);
        abc();

    }

    private void abc() {
        dataVerification.firstMethod(temp);
    }


    public void switchToLoginPage(View view) {

    }

    public void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAccount(View view) {
    dataVerification.createAccount(view);
    }
}