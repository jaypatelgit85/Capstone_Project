package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPassword extends AppCompatActivity {
    Intent intent;

    @BindView(R.id.userResetEmail)
    EditText userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    /**
     * sends reset link ot the registered email
     * @param view
     */
    public void resetUserPassword(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(userEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    // run AsyncTask here.
                                    changeActivity();

                                }
                            }, 1000);

                        }
                    }
                });
    }

    private void changeActivity() {
        intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
