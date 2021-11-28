package com.enhanced.sustenance.ui.recipe_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.enhanced.sustenance.databinding.FragmentRecipeMainBinding;
import com.enhanced.sustenance.ui.ViewPagerAdaptor;
import com.enhanced.sustenance.utils.Utils;
import com.google.android.material.tabs.TabLayout;

public class RecipeMainFragment extends Fragment {
    private FragmentRecipeMainBinding binding;

    private static RecipeMainFragment instance;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipeMainBinding.inflate(inflater, container, false);

        instance = this;

        setAdaptors();

        binding.recipesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.recipesViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.recipesViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.recipesTabLayout.selectTab(binding.recipesTabLayout.getTabAt(position));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideKeyboard(requireActivity());
        setAdaptors();
    }


    private void setAdaptors() {
        ViewPagerAdaptor vpAdaptor = new ViewPagerAdaptor(requireActivity().getSupportFragmentManager(), getLifecycle());
        vpAdaptor.addFragment(new SuggestedRecipeFragment());
        vpAdaptor.addFragment(new FavouriteRecipeFragment());
        vpAdaptor.addFragment(new AllRecipeFragment());

        binding.recipesViewPager.setAdapter(vpAdaptor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void notifyAdaptors() {
        FavouriteRecipeFragment.notifyAdaptors();
        AllRecipeFragment.notifyAdaptors();
        SuggestedRecipeFragment.notifyAdaptors();
    }



}