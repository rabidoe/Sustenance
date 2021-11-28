package com.enhanced.sustenance.ui.shopping_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databinding.ShoppingRecyclerviewLayoutBinding;

import java.util.ArrayList;

    public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ShoppingViewHolder>{
        private final ArrayList<ShoppingItemObject> shoppingObjectArrayList;

        private Context context;

        public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTV, qtyTV, categoryTV, aisleTV;
            public ImageButton deleteBtn;
            public CheckBox checkedBtn;


            public ShoppingViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.nameTV);
                qtyTV = itemView.findViewById(R.id.qtyTV);
                categoryTV = itemView.findViewById(R.id.categoryTV);
                aisleTV = itemView.findViewById(R.id.aisleTV);
                deleteBtn = itemView.findViewById(R.id.deleteBtn);
                checkedBtn = itemView.findViewById(R.id.checkedBtn);
            }
        }

        public ShoppingRecyclerAdapter(Context context, ArrayList<ShoppingItemObject> shoppingObjectArrayList) {
            this.context = context;
            this.shoppingObjectArrayList = shoppingObjectArrayList;
        }

        @NonNull
        @Override
        public ShoppingRecyclerAdapter.ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            com.enhanced.sustenance.databinding.ShoppingRecyclerviewLayoutBinding binding = ShoppingRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            binding.deleteBtn.setOnClickListener(v -> {
                MainActivity.getDBHandler().deleteItemByName(DBHandler.DATABASES.SHOPPING, v.getTag().toString());
                BootSequence.executeShoppingListObjects();
            });
            binding.checkedBtn.setOnClickListener(v -> {
                if (binding.checkedBtn.isChecked()) {
                    MainActivity.getDBHandler().updatedShoppingCheckedState(binding.nameTV.getText().toString(), true);
                } else {
                    MainActivity.getDBHandler().updatedShoppingCheckedState(binding.nameTV.getText().toString(), false);
                }
            });
            return new com.enhanced.sustenance.ui.shopping_fragment.ShoppingRecyclerAdapter.ShoppingViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull com.enhanced.sustenance.ui.shopping_fragment.ShoppingRecyclerAdapter.ShoppingViewHolder holder, int position) {
            ShoppingItemObject currentObj = shoppingObjectArrayList.get(position);
            if (currentObj != null && currentObj.getName() != null) {
                holder.nameTV.setText(currentObj.getName());
                String qty = "Qty: " + currentObj.getQty() + currentObj.getUnit().getValue();
                holder.qtyTV.setText(qty);
                String cat = "Category: " + currentObj.getCategory().getValue();
                holder.categoryTV.setText(cat);
                if (currentObj.getAisle() != null) {
                    String aisle = "Aisle: " + currentObj.getAisle();
                    holder.aisleTV.setText(aisle);
                }
                holder.checkedBtn.setChecked(currentObj.isChecked());
                holder.deleteBtn.setTag(currentObj.getName());
            }
        }

        @Override
        public int getItemCount() {
            return shoppingObjectArrayList.size();
        }
    }

