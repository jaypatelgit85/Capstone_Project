package patel.mohawk.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    ArrayList<String> uid;
    ArrayList<String> name;
    ArrayList<String> email;
    Context mcontext;
    public UsersAdapter(ArrayList<String> uid,ArrayList<String> name,ArrayList<String> email,Context mcontext) {
        this.uid = uid;
        this.name=name;
        this.email=email;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_users,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
            holder.username.setText(name.get(position));
            holder.userEmail.setText(email.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,RemoveEmployeeOrUser.class);
                intent.putExtra("name",name.get(position));
                intent.putExtra("email",email.get(position));
                intent.putExtra("uid",uid.get(position));
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView userEmail;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.listUsersname);
            userEmail = itemView.findViewById(R.id.listUsersEmail);
            parentLayout = itemView.findViewById(R.id.userListLayout);
        }
    }
}
