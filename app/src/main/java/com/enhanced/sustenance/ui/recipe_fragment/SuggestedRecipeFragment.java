package com.enhanced.sustenance.ui.recipe_fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.databinding.FragmentSuggestedRecipeBinding;
import com.enhanced.sustenance.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class SuggestedRecipeFragment extends Fragment {
    private static final String TAG = "SuggestedRecipeFragment";
    private FragmentSuggestedRecipeBinding binding;

    private static SuggestedRecipeFragment instance;

    private static final ArrayList<RecipeObject> recipeObjects = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSuggestedRecipeBinding.inflate(inflater, container, false);

        instance = this;

        RecyclerView.LayoutManager recipeLayoutManager = new LinearLayoutManager(getContext());

        binding.sugRecipeRecyclerView.setLayoutManager(recipeLayoutManager);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideKeyboard(requireActivity());
        new BuildSuggestionList().execute();
    }

    private static class BuildSuggestionList extends AsyncTask<String, String, Boolean> {
        private Object Boolean;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            //reset the array
            recipeObjects.clear();
            //get the sorted score map
            LinkedHashMap<String, Integer> scoreMap = sortMap(buildScoreMap());
            //iterate through each score
            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                //make sure it has a value above 0 (0 means ingredients not avail)
                if (entry.getValue() > 0) {
                    //iterate through the recipe list to find current recipe
                    for (RecipeObject obj : MainActivity.getRecipeObjects()) {
                        if (obj.getName().equalsIgnoreCase(entry.getKey())) {
                            //add recipe to the arrayList to feed to adaptor
                            recipeObjects.add(obj);
                        }
                    }
                }
            }
            return (java.lang.Boolean) Boolean;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            instance.setAdaptors();
            super.onPostExecute((java.lang.Boolean) Boolean);
        }

    }


    private static TreeMap<String, Integer> buildScoreMap() {
        TreeMap<String, Integer> scoreMap = new TreeMap<>();
        for (RecipeObject obj : MainActivity.getRecipeObjects()) {
            String recipeName = obj.getName();
            ArrayList<String[]> ingList = Utils.getIngredientsList(obj);
            int score = 0;
            for (String[] strings : ingList) {
                if (Utils.isIngredientAvailable(strings[0])) {
                    int exp = Utils.daysUntilExpire(strings[0]);
                    if (exp < 4) {
                        exp = exp * 2;
                    }
                    score = score + exp;
                    //add 1 just to move from 0 to as all recipes with 0 score will be excluded
                    score = score + 1;
                }
            }
            scoreMap.put(recipeName, score);
        }
        return scoreMap;
    }

    private ArrayList<RecipeObject> reverseArray(ArrayList<RecipeObject> recipeObjects) {
        ArrayList<RecipeObject> revArrayList = new ArrayList<>();
        for (int i = recipeObjects.size() - 1; i >= 0; i--) {
            if (recipeObjects.size() > i && recipeObjects.get(i) != null) {
                revArrayList.add(recipeObjects.get(i));
            }
        }

        return revArrayList;
    }

    private static LinkedHashMap<String, Integer> sortMap(Map<String, Integer> unSortedMap) {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        unSortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    private void setAdaptors() {
        RecyclerView.Adapter recipeAdaptor = new RecipeRecyclerAdaptor(getContext(), reverseArray(recipeObjects), false);
        if (binding != null) {
            binding.sugRecipeRecyclerView.setAdapter(recipeAdaptor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void notifyAdaptors() {
        if (instance != null) {
            new BuildSuggestionList().execute();
        }
    }



}