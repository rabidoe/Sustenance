package com.enhanced.sustenance.ui.pantry_fragment;

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
import com.enhanced.sustenance.databinding.FragmentPantryBinding;
import com.enhanced.sustenance.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class PantryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PantryFragment";
    private static FragmentPantryBinding binding;

    private static PantryFragment instance;

    private static RecyclerView.Adapter archiveAdaptor, expiredAdaptor, availableAdaptor;
    private RecyclerView.LayoutManager archiveLayoutManager, expiredLayoutManager, availableLayoutManager;

    private ArrayList<FoodObject> expiredFoodObjects = new ArrayList<>();
    private ArrayList<FoodObject> availableFoodObjects = new ArrayList<>();
    private ArrayList<FoodObject> archiveFoodObjects = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(inflater, container, false);

        instance = this;

        archiveLayoutManager = new LinearLayoutManager(getContext());
        expiredLayoutManager = new LinearLayoutManager(getContext());
        availableLayoutManager  = new LinearLayoutManager(getContext());

        binding.archiveRecyclerView.setLayoutManager(archiveLayoutManager);
        binding.expiredRecyclerView.setLayoutManager(expiredLayoutManager);
        binding.availableRecyclerView.setLayoutManager(availableLayoutManager);

        binding.expiredBtn.setOnClickListener(this);
        binding.availableBtn.setOnClickListener(this);
        binding.archiveBtn.setOnClickListener(this);

        binding.addFoodItemBtn.setOnClickListener(this);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        createFoodObjects();
        setAdaptors();
        Utils.hideKeyboard(requireActivity());
    }

    private void setAdaptors() {
        archiveAdaptor = new ArchiveRecyclerViewAdaptor(getActivity(), Utils.SortFoodItemArrayList.alphabetically(archiveFoodObjects));
        expiredAdaptor = new ExpAndAvailRecyclerViewAdaptor(getActivity(), Utils.SortFoodItemArrayList.alphabetically(expiredFoodObjects));
        availableAdaptor = new ExpAndAvailRecyclerViewAdaptor(getActivity(), Utils.SortFoodItemArrayList.alphabetically(availableFoodObjects));

        if (binding != null) {
            binding.archiveRecyclerView.setAdapter(archiveAdaptor);
            binding.expiredRecyclerView.setAdapter(expiredAdaptor);
            binding.availableRecyclerView.setAdapter(availableAdaptor);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createFoodObjects() {
        expiredFoodObjects = new ArrayList<>();
        availableFoodObjects = new ArrayList<>();
        archiveFoodObjects = new ArrayList<>();
        for (FoodObject obj : MainActivity.getFoodObjects()) {
            Date d = new Date();
            if (obj.getExpiry() != null && d.after(obj.getExpiry())) {
                expiredFoodObjects.add(obj);
            } else if (obj.getQty() != null && !obj.getQty().equalsIgnoreCase("") && Integer.parseInt(obj.getQty()) > 0) {
                availableFoodObjects.add(obj);
            } else {
                archiveFoodObjects.add(obj);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.archiveBtn) {
            toggleVisibility(binding.archiveRecyclerView);
        } else if (v == binding.availableBtn) {
            toggleVisibility(binding.availableRecyclerView);
        } else if (v == binding.expiredBtn) {
            toggleVisibility(binding.expiredRecyclerView);
        } else if (v == binding.addFoodItemBtn) {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_pantry_to_nav_item);
        }
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void notifyAdaptors() {
        if (instance != null) {
            instance.createFoodObjects();
            instance.setAdaptors();
        }
    }

}