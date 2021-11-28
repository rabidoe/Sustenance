package com.enhanced.sustenance.ui.recipe_fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databases.JSON;
import com.enhanced.sustenance.databinding.RecipeRecyclerviewLayoutBinding;

import java.util.ArrayList;

    public class RecipeRecyclerAdaptor extends RecyclerView.Adapter<RecipeRecyclerAdaptor.RecipeViewHolder>{
        private final ArrayList<RecipeObject> recipeObjectArrayList;

        private final Context context;
        private final boolean showDelete;

        public static class RecipeViewHolder extends RecyclerView.ViewHolder {
            public TextView recipeNameTextView;
            public ImageButton recipeDeleteBtn;
            public ImageView recipeIV;


            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
                recipeDeleteBtn = itemView.findViewById(R.id.recipeDeleteBtn);
                recipeIV = itemView.findViewById(R.id.recipeIV);
            }
        }

        public RecipeRecyclerAdaptor(Context context, ArrayList<RecipeObject> recipeObjectArrayList, boolean showDelete) {
            this.context = context;
            this.recipeObjectArrayList = recipeObjectArrayList;
            this.showDelete = showDelete;
        }

        @NonNull
        @Override
        public RecipeRecyclerAdaptor.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecipeRecyclerviewLayoutBinding binding = RecipeRecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

            binding.getRoot().setOnClickListener(v -> {
                RecipeObject recipeObject = null;
                for (RecipeObject obj : recipeObjectArrayList) {
                    if (obj.getName().matches(binding.recipeNameTextView.getText().toString())) {
                        recipeObject = obj;
                        break;
                    }
                }

                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                if (recipeObject != null) {
                    bundle = new Bundle();
                    bundle.putParcelable("recipeObject", recipeObject);
                }
                navController.navigate(R.id.action_nav_recipe_to_nav_new_recipe, bundle);

            });



            return new RecipeRecyclerAdaptor.RecipeViewHolder(binding.getRoot());
        }


        @Override
        public void onBindViewHolder(@NonNull RecipeRecyclerAdaptor.RecipeViewHolder holder, int position) {
            RecipeObject currentObj = recipeObjectArrayList.get(position);
            if (currentObj != null && currentObj.getName() != null) {
                holder.recipeNameTextView.setText(currentObj.getName());
                if (showDelete) {
                    holder.recipeDeleteBtn.setVisibility(View.VISIBLE);
                    holder.recipeDeleteBtn.setOnClickListener(v -> {
                        MainActivity.getDBHandler().deleteItemByName(DBHandler.DATABASES.RECIPES, currentObj.getName());
                        JSON.deleteImage(context, currentObj.getName());
                        BootSequence.executeRecipeList();
                    });
                } else {
                    holder.recipeDeleteBtn.setVisibility(View.GONE);
                }
                Runnable runnable = () -> {
                    BitmapDrawable bmd = JSON.getImage(context, currentObj.getName());
                    if (bmd != null) {
                        Bitmap bm = bmd.getBitmap();
                        MainActivity.getUIhandler().post(() -> {
                            holder.recipeIV.setImageBitmap(bm);
                            holder.recipeIV.setBackground(null);
                        });
                    }
                };
                MainActivity.getHandler().post(runnable);
            }
        }

        @Override
        public int getItemCount() {
            return recipeObjectArrayList.size();
        }
    }
