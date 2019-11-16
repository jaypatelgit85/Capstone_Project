package patel.mohawk.capstoneproject.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import patel.mohawk.capstoneproject.HomePage;
import patel.mohawk.capstoneproject.R;
import patel.mohawk.capstoneproject.RecyclerViewAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Bitmap> mImages = new ArrayList<>();

    View intialRoot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        intialRoot = root;
        initBitmapImages();
        return root;
    }




    private void initBitmapImages(){

        mNames.add("The Avengers: End Game");
        mNames.add("Life Of Pi");





        new DownloadImageTask().execute("https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SX300.jpg","https://m.media-amazon.com/images/M/MV5BNTg2OTY2ODg5OF5BMl5BanBnXkFtZTcwODM5MTYxOA@@._V1_SX300.jpg");

//            initRecyleerView();


    }

    private void initRecyleerView(){

        RecyclerView recyclerView = intialRoot.findViewById(R.id.moviesListRecyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mNames,mImages,intialRoot.getContext());

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(intialRoot.getContext()));

    }

    private class DownloadImageTask extends AsyncTask<String , Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bmp = null;

            try {
                URL url = null;
                for(int i =0; i<strings.length;i++){
                    url = new URL(strings[i]);

                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    mImages.add(bmp);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Find the ImageView object to use


                    initRecyleerView();


            }
        }

    }


}