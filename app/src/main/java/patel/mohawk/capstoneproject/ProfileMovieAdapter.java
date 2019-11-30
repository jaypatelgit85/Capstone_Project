package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.BitSet;

public class ProfileMovieAdapter extends RecyclerView.Adapter<ProfileMovieAdapter.ViewHolder>{

    ArrayList<String> movieName;
    ArrayList<Bitmap> moviePoster;
    ArrayList<String> movieId;
    String where;
    private Context mcontext;
    public ProfileMovieAdapter(ArrayList<String> movieName,ArrayList<Bitmap> moviePoster,ArrayList<String> movieId,String where,Context mcontext) {
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.where = where;
        this.movieId = movieId;
        this.mcontext = mcontext;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_movies_display,parent,false);
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
        holder.movieTitle.setText(movieName.get(position));
        holder.moviePoster.setImageBitmap(moviePoster.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(where.equals("rent")){
                    Intent intent = new Intent(mcontext,ShowMovieRent.class);
                    intent.putExtra("movieName",movieName.get(position));
                    intent.putExtra("id",movieId.get(position));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    moviePoster.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image",byteArray);
                    mcontext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mcontext,ShowMovieFav.class);
                    intent.putExtra("movieName",movieName.get(position));
                    intent.putExtra("id",movieId.get(position));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    moviePoster.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image",byteArray);
                    mcontext.startActivity(intent);


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieName.size();
    }

    /**
     * sets up the variable for the data
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        ImageView moviePoster;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieNameCardView);
            moviePoster= itemView.findViewById(R.id.moviePosterCardView);
            parentLayout = itemView.findViewById(R.id.profileMovies);
        }
    }
}
