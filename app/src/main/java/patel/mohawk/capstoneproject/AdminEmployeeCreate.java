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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminEmployeeCreate extends AppCompatActivity {

    @BindView(R.id.inputEmployeeFullName)
    EditText userFullName;
    @BindView(R.id.inputEmployeePhoneNumber)
    EditText phoneNumber;
    @BindView(R.id.inputEmployeeEmail)
    EditText userEmail;
    @BindView(R.id.inputEmployeePassword)
    EditText userPassword;
    @BindView(R.id.btnEmployeeSignup)
    Button btnEmployeeSignUp;
    ArrayList<EditText> temp;
    DataVerification dataVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_create);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        temp = new ArrayList<>();
        temp.add(userFullName);
        temp.add(userEmail);
        temp.add(userPassword);
        temp.add(phoneNumber);
        dataVerification = new DataVerification(temp, true);
        firstMethod(temp);

    }

    private void firstMethod(final ArrayList<EditText> temp) {

        dataVerification.firstMethod(temp);
    }

    public void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * calls data verification calss to verify teh dtaa and add a new account to the database
     * @param view
     */
    public void createEmployeeAccount(View view) {
        if (dataVerification.createAccount()) {
            changeActivity();
        }
    }
}

