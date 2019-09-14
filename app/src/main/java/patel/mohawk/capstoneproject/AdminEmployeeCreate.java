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
    EditText userSecurityQuestion;
    @BindView(R.id.btnEmployeeSignup)
    Button signUp;
    ArrayList<EditText> temp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_create);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        temp= new ArrayList<>();
        temp.add(userFullName);
        temp.add(userEmail);
        temp.add(userPassword);
        temp.add(phoneNumber);
        signUp.setEnabled(false);
        firstMethod(temp);

    }

    private void firstMethod(final ArrayList<EditText> temp){

//        mAuth = FirebaseAuth.getInstance();
//        for(int i=0;i<temp.size();i++){
//
//            final int finalI = i;
//
//            temp.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(!hasFocus){
//                        textInputValidation(temp.get(finalI));
//
//                        if(finalI==1){
//                            emailValidation(temp.get(1));
//                        }
//                    }
//                }
//
//
//            });
//
//            temp.get(i).addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    Boolean signUpButtonEnabler=finalValidation(temp);
//                    if ((signUpButtonEnabler)) {
//                        signUp.setEnabled(true);
//                    }else{
//                        signUp.setEnabled(false);
//                    }
//
//                }
//            });
//
//
//
//        }

  }

    /**
     *
     * @param editText
     */
    private void emailValidation(EditText editText) {
        if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()){
            editText.setError("Invalid Email Address");
        }

    }

    /**
     *
     * @param editText
     */
    private void textInputValidation(EditText editText){
        if(editText.getText().toString().matches("")){
            editText.setError("Required");
        }

    }

    /**
     *Doesnt Work yet
     * To make sure that signup button become available
     * @param
     * @return
     */
    private Boolean finalValidation(ArrayList<EditText> editTexts) {
        boolean temp=false;
        for(int i=0;i<editTexts.size();i++){
            if(!editTexts.get(i).getText().toString().matches("")){
                temp=true;
                if(i==1){
                    if(!Patterns.EMAIL_ADDRESS.matcher(editTexts.get(1).getText().toString()).matches()){
                        temp=false;
                        break;
                    }else {
                        temp=true;
                    }
                }
            }
            else{
                temp=false;
                break;
            }
        }

        return temp;
    }


    /**
     * Send Data to database for storing after validating
     * @param view
     */
    public void createEmployeeAccount(View view) {
        try {


            mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Create User", "createUserWithEmail:success");
                                addUserData();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        // run AsyncTask here.
                                        changeActivity();

                                    }
                                }, 1000);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Create User", "createUserWithEmail:failure", task.getException());

                            }

                            // ...
                        }


                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addUserData() {
        FirebaseUser user=mAuth.getCurrentUser();
        Map<String, Object> users = new HashMap<>();
        users.put("fullName",userFullName.getText().toString());
        users.put("phoneNumber",phoneNumber.getText().toString());
        users.put("email",userEmail.getText().toString());
        users.put("admin",false);
        users.put("employee",true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(user.getUid()).document("userLogin")
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });



    }

    private void changeActivity() {
        Intent myIntent=new Intent(this,MainActivity.class);
        startActivity(myIntent);

    }

    public void switchToLoginPage(View view) {
        changeActivity();
    }

}
