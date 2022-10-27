package com.hobby_projects.retrofit_rest_api.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hobby_projects.retrofit_rest_api.Model.Items;
import com.hobby_projects.retrofit_rest_api.R;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements Filterable {
    // creating a variable for array list and context.
    private ArrayList<Items> ItemsArrayList, ItemsArrayListFiltered;
    private Context context;
    ArrayList<Items> finalFilteredList = new ArrayList<>();

    itemsCallback callBack;

    public void setCallBack(itemsCallback callBack){
        this.callBack = callBack;
    }

    public ItemsAdapter(ArrayList<Items> ItemsArrayList, Context context) {
        this.ItemsArrayList = ItemsArrayList;
        this.ItemsArrayListFiltered = ItemsArrayList;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ItemsArrayListFiltered = ItemsArrayList;
                } else {
                    ArrayList<Items> filteredList = new ArrayList<>();
                    for (Items row : ItemsArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (
                                row.getName().toLowerCase().contains(charString) ||
                                row.getDescription().toLowerCase().contains(charString)
                        ) {
                            filteredList.add(row);
                        }
                    }
                    ItemsArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ItemsArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ItemsArrayListFiltered = (ArrayList<Items>) filterResults.values;
                notifyDataSetChanged();
                finalFilteredList = ItemsArrayListFiltered;
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_card, parent, false);
        return new ItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int ppp) {
        int position = ppp;
        Items item = ItemsArrayListFiltered.get(position);
        holder.name.setText("("+item.getId()+") "+item.getName());
        holder.info.setOnClickListener(v -> {
            callBack.infoItem(context,item, position);
        });
        holder.edit.setOnClickListener(v -> {
            callBack.editItem(context,item, position);
        });
        holder.delete.setOnClickListener(v -> {
            callBack.deleteItem(context,item, position);
        });

    }

    @Override
    public int getItemCount() {
        return (ItemsArrayListFiltered == null) ? 0 : ItemsArrayListFiltered.size();
    }

    public ArrayList<Items> getFilteredData() {
        return ItemsArrayListFiltered;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView info, edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            info = itemView.findViewById(R.id.info);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface itemsCallback {
        public void infoItem(Context context, Items i, int position);
        public void editItem(Context context, Items i, int position);
        public void deleteItem(Context context, Items i, int position);
    }
}

