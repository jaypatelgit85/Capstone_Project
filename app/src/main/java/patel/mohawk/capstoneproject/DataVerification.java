package patel.mohawk.capstoneproject;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
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


class DataVerification{

    private FirebaseAuth mAuth;
    SignUp signUp=new SignUp();
    private ArrayList<EditText> userData;
    private boolean isEmployee;

    DataVerification(ArrayList<EditText> temp,boolean isEmployee) {
        userData = temp;
        this.isEmployee = isEmployee;
    }
    void firstMethod(final ArrayList<EditText> temp){

        for(int i=0;i<temp.size();i++){

            final int finalI = i;

            temp.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        textInputValidation(temp.get(finalI));

                        if(finalI==1){
                            emailValidation(temp.get(1));
                        }
                    }
                }


            });

            temp.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //signUp.setSignUpButtonEnabler(finalValidation(userData));
                }

                @Override
                public void afterTextChanged(Editable s) {
                  finalValidation(userData);
                }
            });



        }
    }

    /**
     *
     * @param editText
     */
    void emailValidation(EditText editText) {
        if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()){
            editText.setError("Invalid Email Address");
        }

    }

    /**
     *
     * @param editText
     */
    void textInputValidation(EditText editText){
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
    private void finalValidation(ArrayList<EditText> editTexts) {
        boolean temp=false;
        for(int i=0;i<editTexts.size();i++){
            if(!editTexts.get(i).getText().toString().matches("")){
                temp=true;
                if(i==1){
                    if(!Patterns.EMAIL_ADDRESS.matcher(editTexts.get(1).getText().toString()).matches()){
                        temp=false;
                        break;
                    }
                }
            }
            else{
                temp=false;
                break;
            }
        }
        signUp.abb(temp);
    }


    /**
     * Send Data to database for storing after validating
     *
     * @return
     */
    boolean createAccount() {
        mAuth=FirebaseAuth.getInstance();
        try {
            Log.d("Flag","in create account try");
            mAuth.createUserWithEmailAndPassword(userData.get(1).getText().toString(), userData.get(2).getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Flag","in on complete");
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Create User", "createUserWithEmail:success");
                                addUserData();
                                verifyUseEmail();
                                // If sign in fails, display a message to the user.
                                Log.w("Create User", "createUserWithEmail:failure", task.getException());
                            }

                            // ...
                        }


                    });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    private void addUserData() {
        FirebaseUser user=mAuth.getCurrentUser();
        Map<String, Object> users = new HashMap<>();
        users.put("fullName",userData.get(0).getText().toString());
        users.put("phoneNumber",userData.get(3).getText().toString());
        users.put("email",userData.get(1).getText().toString());
        users.put("admin",false);
        users.put("employee",isEmployee);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(isEmployee){
            assert user != null;
            db.collection("Employees").document(userData.get(1).getText().toString())
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
        }else{
            assert user != null;
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



    }

    private void verifyUseEmail(){
        final FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        if(!isEmployee )
        {
            user.sendEmailVerification()
                    .addOnCompleteListener((OnCompleteListener) task -> {
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Log.d("Flag","Email Sent");
                        } else {

                            Log.e("", "sendEmailVerification", task.getException());
                            Log.d("Flag","Email Sent");
                        }
                    });
        }


    }



}
