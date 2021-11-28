package com.enhanced.sustenance.ui.pantry_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databinding.FoodItemRecyclerviewLayoutBinding;

import java.util.ArrayList;

public class ArchiveRecyclerViewAdaptor extends RecyclerView.Adapter<ArchiveRecyclerViewAdaptor.ArchiveViewHolder>{
    private final ArrayList<FoodObject> foodObjectArrayList;
    private Context mContext;

    public static class ArchiveViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV;
        public ImageButton deleteBtn;

        public ArchiveViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    public ArchiveRecyclerViewAdaptor(Context context, ArrayList<FoodObject> foodObjectArrayList) {
        this.foodObjectArrayList = foodObjectArrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.enhanced.sustenance.databinding.FoodItemRecyclerviewLayoutBinding binding = FoodItemRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(v -> {
            TextView tv = v.findViewById(R.id.nameTV);
            String name = tv.getText().toString();
            FoodObject foodObject = null;
            for (FoodObject obj : foodObjectArrayList) {
                if (obj.getName().matches(name)) {
                    foodObject = obj;
                    break;
                }
            }
            NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_fragment_content_main);
            Bundle bundle = new Bundle();
            if (foodObject != null) {
                bundle = new Bundle();
                bundle.putParcelable("foodObject", foodObject);
            }
            navController.navigate(R.id.action_nav_pantry_to_nav_item, bundle);
        });
        binding.deleteBtn.setOnClickListener(v -> {
            MainActivity.getDBHandler().deleteItemByName(DBHandler.DATABASES.FOOD_ITEMS, v.getTag().toString());
            BootSequence.executeFoodObjects();
        });
        return new ArchiveViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveViewHolder holder, int position) {
        FoodObject currentObj = foodObjectArrayList.get(position);
        if (currentObj != null && currentObj.getName() != null) {
            holder.nameTV.setText(currentObj.getName());
            holder.deleteBtn.setTag(currentObj.getName());
        }
    }

    @Override
    public int getItemCount() {
        return foodObjectArrayList.size();
    }
}
