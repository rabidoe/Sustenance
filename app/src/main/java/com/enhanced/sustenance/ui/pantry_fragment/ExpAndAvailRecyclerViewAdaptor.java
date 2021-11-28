package com.enhanced.sustenance.ui.pantry_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
import com.enhanced.sustenance.databinding.FoodItemRecyclerviewLayoutBinding;

import java.util.ArrayList;
import java.util.Date;

public class ExpAndAvailRecyclerViewAdaptor extends RecyclerView.Adapter<ExpAndAvailRecyclerViewAdaptor.ExpAndAvailViewHolder>{
    private static final String TAG = "ExpAndAvlRcyclrViwAdptr";
    private final ArrayList<FoodObject> foodObjectArrayList;
    private Context mContext;

    public static class ExpAndAvailViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV, qtyTV, expiryTV;
        public ImageButton deleteBtn;

        public ExpAndAvailViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            qtyTV = itemView.findViewById(R.id.qtyTV);
            expiryTV = itemView.findViewById(R.id.expiryTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    public ExpAndAvailRecyclerViewAdaptor(Context context, ArrayList<FoodObject> foodObjectArrayList) {
        this.foodObjectArrayList = foodObjectArrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public ExpAndAvailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.enhanced.sustenance.databinding.FoodItemRecyclerviewLayoutBinding binding = FoodItemRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(v -> {
            FoodObject foodObject = null;
            for (FoodObject obj : foodObjectArrayList) {
                if (obj.getName().matches(binding.nameTV.getText().toString())) {
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
            FoodObject foodObject = null;
            for (FoodObject obj : foodObjectArrayList) {
                if (obj.getName().matches(binding.nameTV.getText().toString())) {
                    foodObject = obj;
                    foodObject.setQty("");
                    foodObject.setExpiry(null);
                    MainActivity.getDBHandler().handleFoodItem(foodObject);
                    break;
                }
            }
            BootSequence.executeFoodObjects();
        });
        return new ExpAndAvailViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ExpAndAvailViewHolder holder, int position) {
        FoodObject currentObj = foodObjectArrayList.get(position);
        if (currentObj != null && currentObj.getName() != null) {
            holder.nameTV.setText(currentObj.getName());
            holder.qtyTV.setText("Qty: " + currentObj.getQty() + currentObj.getUnit());
            holder.deleteBtn.setTag(currentObj.getName());
            if (currentObj.getExpiry() != null) {
                holder.expiryTV.setText("Expiry: " + currentObj.getExpiryAsString());
                holder.expiryTV.setVisibility(View.VISIBLE);
                Date d = new Date();
                if (d.after(currentObj.getExpiry())) {
                    holder.expiryTV.setTextColor(Resources.getSystem().getColor(android.R.color.holo_red_dark));
                } else if (new Date(d.getTime() + 604800000L).after(currentObj.getExpiry())) {
                    holder.expiryTV.setTextColor(Resources.getSystem().getColor(android.R.color.holo_orange_light));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return foodObjectArrayList.size();
    }
}
