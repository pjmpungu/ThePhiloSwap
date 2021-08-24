package com.thephiloswap.thephiloswapbeta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//this is the array adapter for the list of available books, basically takes the raw
//array list and puts it in a format using the list item xml file in resources

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

    private ArrayList<Book> books;
    ItemClicked activity;
    boolean userBooks;

    public BookAdapter(Context context, ArrayList<Book> list, boolean userBooks){
        books=list;
        activity = (ItemClicked) context;
        this.userBooks = userBooks;
    }

    public interface ItemClicked {
        void onItemClicked(int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvAuthor;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvListItemTitle);
            tvAuthor = itemView.findViewById(R.id.tvListItemAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(books.indexOf((Book) itemView.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(books.get(position));

        holder.tvTitle.setText(books.get(position).getTitle());
        holder.tvAuthor.setText(books.get(position).getAuthor());
        holder.itemView.setVisibility(View.GONE);

        //if the book owner is the same as the user, they won't see it

        if(!books.get(position).getOwnerEmail().equals(ApplicationClass.user.getEmail()) && !userBooks){

            holder.itemView.setVisibility(View.VISIBLE);

        }else if(books.get(position).getOwnerEmail().equals(ApplicationClass.user.getEmail()) && userBooks){

            holder.itemView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
