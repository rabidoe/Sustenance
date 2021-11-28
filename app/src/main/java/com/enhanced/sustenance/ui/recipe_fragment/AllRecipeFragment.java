package com.enhanced.sustenance.ui.recipe_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databinding.FragmentAllRecipeBinding;
import com.enhanced.sustenance.utils.Utils;


public class AllRecipeFragment extends Fragment {

    private FragmentAllRecipeBinding binding;

    private static AllRecipeFragment instance;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllRecipeBinding.inflate(inflater, container, false);

        instance = this;

        RecyclerView.LayoutManager recipeLayoutManager = new LinearLayoutManager(getContext());

        binding.allRecipeRecyclerView.setLayoutManager(recipeLayoutManager);

        binding.recipeAllAddBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_recipe_to_nav_new_recipe);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdaptors();
        Utils.hideKeyboard(requireActivity());
    }

    private void setAdaptors() {
        RecyclerView.Adapter recipeAdaptor = new RecipeRecyclerAdaptor(getContext(), Utils.SortRecipeItemArrayList.alphabetically(MainActivity.getRecipeObjects()), true);
        if (binding != null) {
            binding.allRecipeRecyclerView.setAdapter(recipeAdaptor);
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