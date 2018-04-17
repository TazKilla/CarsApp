package com.musala.groche.carsapp.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.BaseItem;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<BaseItem> itemsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView description;
        public TextView dot;

        ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.label);
            description = view.findViewById(R.id.description);
            dot = view.findViewById(R.id.dot);
        }
    }

    public ItemsAdapter(List<BaseItem> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseItem item = itemsList.get(position);
        holder.label.setText(item.getLabel());
        holder.description.setText(item.getDescription());
        holder.dot.setText(Html.fromHtml("&#8226;"));
    }

    @Override
    public int getItemCount() {
        if (itemsList != null) {
            return itemsList.size();
        } else {
            return 0;
        }
    }
}
