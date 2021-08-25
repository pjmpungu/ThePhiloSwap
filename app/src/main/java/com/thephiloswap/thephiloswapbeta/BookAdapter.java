package com.thephiloswap.thephiloswapbeta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//this is the array adapter for the list of available books, basically takes the raw
//array list and puts it in a format using the list item xml file in resources

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements Filterable {

    private ArrayList<Book> books;
    //following list used for searching for items.
    private ArrayList<Book> booksFull;
    ItemClicked activity;
    boolean userBooks;

    public BookAdapter(Context context, ArrayList<Book> list, boolean userBooks){
        books = list;
        this.booksFull = new ArrayList<>(books);
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
            holder.tvAuthor.setText("By " + books.get(position).getAuthor());

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    //method from interface to do a search

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Book> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){

                filteredList.addAll(booksFull);

            }else{

                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Book book: booksFull){
                    if(book.getTitle().toLowerCase().contains(filterPattern) ||
                            book.getAuthor().toLowerCase().contains(filterPattern) ||
                    book.getKeywords().toLowerCase().contains(filterPattern)){
                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            books.clear();
            books.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}
