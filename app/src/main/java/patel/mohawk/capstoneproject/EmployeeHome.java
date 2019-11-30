package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import patel.mohawk.capstoneproject.AddNewMovie;
import patel.mohawk.capstoneproject.HomePage;
import patel.mohawk.capstoneproject.ManageUsers;
import patel.mohawk.capstoneproject.R;

public class EmployeeHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
    }

    public void userManagement(View view) {
        Intent intent = new Intent(this, ManageUsers.class);
        startActivity(intent);
    }

    public void homePage(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void addMOvieEmployee(View view) {
        Intent intent = new Intent(this, AddNewMovie.class);
        startActivity(intent);
    }
}
