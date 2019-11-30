package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import android.app.Activity;
import  android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;

import patel.mohawk.capstoneproject.ui.home.HomeFragment;
import patel.mohawk.capstoneproject.ui.profile.ProfileFragment;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mMovieNames;
    private ArrayList<Bitmap> mMoviePosters;
    private Context mcontext;
    private JSONArray searchResults;


    public RecyclerViewAdapter(ArrayList<String> mMovieNames, ArrayList<Bitmap> mMoviePosters, Context mcontext,JSONArray searchResults) {
        this.mMovieNames = mMovieNames;
        this.mMoviePosters = mMoviePosters;
        this.mcontext = mcontext;
        this.searchResults = searchResults;


        Log.d("size",this.mMovieNames.size()+"");
    }
    /**
     * creates the view to addd teh data for teh profile details
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    /**
     * sets the data in teh respected fields for the array that is recieved and sets on click listener for further actions
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


      holder.moviePoster.setImageBitmap(mMoviePosters.get(position));
        holder.movieName.setText(mMovieNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,ShowMovie.class);
                intent.putExtra("jsonArray",searchResults.toString());
                intent.putExtra("position",position);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        ImageView moviePoster;
        TextView movieName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieName = itemView.findViewById(R.id.movieName);
            parentLayout = itemView.findViewById(R.id.listItems);

        }
    }

}
