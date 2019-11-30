package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity {
    @BindView(R.id.profileFullName)
    EditText userFullName;
    @BindView(R.id.profileEmail)
    EditText userEmail;
    @BindView(R.id.profilePassword)
    EditText userPassword;
    @BindView(R.id.profilePhoneNumber)
    EditText phoneNumber;
    @BindView(R.id.btn_changeProfile)
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

        dataVerification = new DataVerification(temp,false);
        abc();

    }

    private void abc() {
            dataVerification.firstMethod(temp);
    }

    /**
     * takes back to login page
     * @param view
     */
    public void switchToLoginPage(View view) {
        changeActivity();
    }

    /**
     * changes the activity to login page
     */
    public void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * send the user data to data verification class to create a new use
     * @param view
     */
    public void createAccount(View view) {
    if(dataVerification.createAccount()){
        changeActivity();
    }
    }

}