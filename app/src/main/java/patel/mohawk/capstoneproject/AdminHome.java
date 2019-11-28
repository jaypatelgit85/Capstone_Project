package patel.mohawk.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void goToUserHome(View view) {
        Intent intent = new Intent(this,HomePage.class);
        startActivity(intent);
    }

    public void GoToManageEmployee(View view) {

    }

    public void addNewEmployee(View view) {
        Intent intent = new Intent(this,AdminEmployeeCreate.class);
        startActivity(intent);

    }

    public void manageUsers(View view) {

    }

    public void addMovies(View view) {
        Intent intent = new Intent(this,AddNewMovie.class);
        startActivity(intent);
    }
}
