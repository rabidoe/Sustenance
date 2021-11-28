package com.enhanced.sustenance.ui.shopping_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.databinding.FragmentShoppingMainBinding;
import com.enhanced.sustenance.utils.Utils;

public class ShoppingMainFragment extends Fragment {
    private static final String TAG = "ShoppingMainFragment";
    private FragmentShoppingMainBinding binding;

    private static ShoppingMainFragment instance;

    private static RecyclerView.Adapter shoppingAdaptor;
    private RecyclerView.LayoutManager shoppingLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingMainBinding.inflate(inflater, container, false);

        instance = this;

        shoppingLayoutManager = new LinearLayoutManager(getContext());

        binding.shoppligRecyclerView.setLayoutManager(shoppingLayoutManager);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdaptors();
        Utils.hideKeyboard(requireActivity());
    }

    private void setAdaptors() {
        shoppingAdaptor = new ShoppingRecyclerAdapter(getContext(), Utils.SortShoppingItemArrayList.alphabetically(MainActivity.getShoppingItemObjects()));
        if (binding != null) {
            binding.shoppligRecyclerView.setAdapter(shoppingAdaptor);
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