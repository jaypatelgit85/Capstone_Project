package patel.mohawk.capstoneproject;

import  android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mMovieNames;
    private ArrayList<Bitmap> mMoviePosters;
    private Context mcontext;


    public RecyclerViewAdapter(ArrayList<String> mMovieNames, ArrayList<Bitmap> mMoviePosters, Context mcontext) {
        this.mMovieNames = mMovieNames;
        this.mMoviePosters = mMoviePosters;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("newItem",mMoviePosters.size()+"");

        holder.moviePoster.setImageBitmap(mMoviePosters.get(position));
        holder.movieName.setText(mMovieNames.get(position));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Abc", "onClick: clicked on: " + mMovieNames.get(position));
                Toast.makeText(mcontext, mMovieNames.get(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


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
