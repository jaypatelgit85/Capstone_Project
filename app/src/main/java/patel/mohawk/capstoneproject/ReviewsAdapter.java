package patel.mohawk.capstoneproject;
// I Jay Kumar Patel,000744834 have done this assignment by my own and haven't copied it from anywhere.
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {


    private ArrayList<String> reviews;

    // Pass in the contact array into the constructor
    public ReviewsAdapter(ArrayList<String> reviews) {
        this.reviews = reviews;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_review,parent,false);
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
        holder.reviewsUser.setText(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewsUser;
        public ViewHolder(View itemView) {
            super(itemView);
            reviewsUser = itemView.findViewById(R.id.userComment);
        }
    }
}
