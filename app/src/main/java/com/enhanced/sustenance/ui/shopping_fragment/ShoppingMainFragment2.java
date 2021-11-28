package com.enhanced.sustenance.ui.shopping_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.enhanced.sustenance.databinding.FragmentShoppingMain2Binding;
import com.enhanced.sustenance.ui.ViewPagerAdaptor;
import com.google.android.material.tabs.TabLayout;

public class ShoppingMainFragment2 extends Fragment {
    private FragmentShoppingMain2Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingMain2Binding.inflate(inflater, container, false);

        ViewPagerAdaptor vpAdaptor = new ViewPagerAdaptor(requireActivity().getSupportFragmentManager(), getLifecycle());
        vpAdaptor.addFragment(new ShoppingCategoriesFragment());
        vpAdaptor.addFragment(new ShoppingListFragment());

        binding.shoppingViewPager.setAdapter(vpAdaptor);

        binding.shoppingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.shoppingViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.shoppingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.shoppingTabLayout.selectTab(binding.shoppingTabLayout.getTabAt(position));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}