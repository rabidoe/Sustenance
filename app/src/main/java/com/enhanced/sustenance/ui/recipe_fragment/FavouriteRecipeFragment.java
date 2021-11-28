package com.enhanced.sustenance.ui.recipe_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.databinding.FragmentFavouriteRecipeBinding;
import com.enhanced.sustenance.utils.Utils;

public class FavouriteRecipeFragment extends Fragment {

    private FragmentFavouriteRecipeBinding binding;

    private static  FavouriteRecipeFragment instance;

    private static RecyclerView.Adapter recipeAdaptor;
    private RecyclerView.LayoutManager recipeLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteRecipeBinding.inflate(inflater, container, false);

        instance = this;

        recipeLayoutManager = new LinearLayoutManager(getContext());

        binding.favRecipeRecyclerView.setLayoutManager(recipeLayoutManager);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdaptors();
        Utils.hideKeyboard(requireActivity());
    }

    private void setAdaptors() {
        recipeAdaptor = new RecipeRecyclerAdaptor(getContext(), Utils.SortRecipeItemArrayList.alphabetically(MainActivity.getFavRecipeObjects()), false);
        if (binding != null) {
            binding.favRecipeRecyclerView.setAdapter(recipeAdaptor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void notifyAdaptors() {
        if (instance != null) {
            instance.setAdaptors();
        }
    }
}