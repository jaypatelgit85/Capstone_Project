package patel.mohawk.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity {
    @BindView(R.id.inputFullName)
    EditText userFullName;
    @BindView(R.id.inputEmail)
    EditText userEmail;
    @BindView(R.id.inputPassword)
    EditText userPassword;
    @BindView(R.id.inputPhoneNumber)
    EditText phoneNumber;
    @BindView(R.id.btn_signup)
    Button signUp;
    ArrayList<EditText> temp;
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
        temp.add(phoneNumber);
        signUp.setEnabled(false);
        dataVerification = new DataVerification(temp,false);
        abc();

    }

    private void abc() {
            dataVerification.firstMethod(temp);
    }


    public void switchToLoginPage(View view) {
        changeActivity();
    }

    public void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAccount(View view) {
    if(dataVerification.createAccount()){
        changeActivity();
    }
    }
    public void abb(boolean temp){
        Log.d("booleanaaaaa",""+temp);
    /* if(temp){
         signUp.setEnabled(temp);
     }*/

    }
}