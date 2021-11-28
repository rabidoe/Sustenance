package com.enhanced.sustenance.ui.pantry_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databinding.FragmentFoodItemBinding;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingItemObject;
import com.enhanced.sustenance.utils.Enums;
import com.enhanced.sustenance.utils.Utils;

public class FoodItemFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FoodItemFragment";
    private FragmentFoodItemBinding binding;

    private ViewGroup container;
    private DBHandler dbHandler;

    private ArrayAdapter<Enums.UNITS> foodItemUnitSpinnerAdaptor;
    private ArrayAdapter<Enums.CATEGORIES> foodItemCatSpinnerAdaptor;
    private ArrayAdapter<Enums.PACKAGING> foodItemPackSpinnerAdaptor;
    private ArrayAdapter<Enums.UNITS> shoppingItemUnitSpinnerAdaptor;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodItemBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.container = container;
        container.setVisibility(View.VISIBLE);

        dbHandler = MainActivity.getDBHandler();

        foodItemUnitSpinnerAdaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, Enums.UNITS.values());
        foodItemCatSpinnerAdaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, Enums.CATEGORIES.values());
        foodItemPackSpinnerAdaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, Enums.PACKAGING.values());
        shoppingItemUnitSpinnerAdaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, Enums.UNITS.values());

        binding.foodItemUnitSpinner.setAdapter(foodItemUnitSpinnerAdaptor);
        binding.foodItemCatSpinner.setAdapter(foodItemCatSpinnerAdaptor);
        binding.foodItemPackSpinner.setAdapter(foodItemPackSpinnerAdaptor);
        binding.foodItemShoppingUnitSpinner.setAdapter(shoppingItemUnitSpinnerAdaptor);
        binding.foodItemUnitSpinner.setSelection(0);
        binding.foodItemCatSpinner.setSelection(0);
        binding.foodItemPackSpinner.setSelection(0);
        binding.foodItemShoppingUnitSpinner.setSelection(0);

        binding.foodItemSaveButton.setOnClickListener(this);
        binding.shoppingAddButton.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            FoodObject obj = bundle.getParcelable("foodObject");
            if (obj != null) {
                binding.itemNameEditText.setText(obj.getName());
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.foodItemSaveButton) {
            if (!Utils.isEmpty(binding.itemNameEditText)) {
                FoodObject obj = new FoodObject(binding.itemNameEditText.getText().toString());
                if (!Utils.isEmpty(binding.foodItemQtyET)) {
                    obj.setQty(binding.foodItemQtyET.getText().toString());
                    obj.setUnit(Enums.UNITS.getEnum(binding.foodItemUnitSpinner.getSelectedItem().toString()));
                }
                if (!Utils.isEmpty(binding.editTextDateDay) && !Utils.isEmpty(binding.editTextDateMonth) && !Utils.isEmpty(binding.editTextDateYear)) {
                    obj.setExpiryFromStrings(binding.editTextDateDay.getText().toString(), binding.editTextDateMonth.getText().toString(), binding.editTextDateYear.getText().toString());
                }
                if (!Utils.isEmpty(binding.isleEditText)) {
                    obj.setIsle(binding.isleEditText.getText().toString());
                }
                obj.setCategory(Enums.CATEGORIES.getEnum(binding.foodItemCatSpinner.getSelectedItem().toString()));
                obj.setPackaging(Enums.PACKAGING.getEnum(binding.foodItemPackSpinner.getSelectedItem().toString()));
                if (!Utils.isEmpty(binding.foodItemNotesEditText)) {
                    obj.setNote(binding.foodItemNotesEditText.getText().toString());
                }
                dbHandler.handleFoodItem(obj);
                BootSequence.executeFoodObjects();
            }
        } else if (v == binding.shoppingAddButton) {
            if (!Utils.isEmpty(binding.itemNameEditText) && !Utils.isEmpty(binding.shoppingQtyET)) {
                ShoppingItemObject shoppingItemObject = new ShoppingItemObject(binding.itemNameEditText.getText().toString());
                shoppingItemObject.setQty(binding.shoppingQtyET.getText().toString());
                shoppingItemObject.setUnit(Enums.UNITS.getEnum(binding.foodItemShoppingUnitSpinner.getSelectedItem().toString()));
                shoppingItemObject.setCategory(Enums.CATEGORIES.getEnum(binding.foodItemCatSpinner.getSelectedItem().toString()));
                if (!Utils.isEmpty(binding.isleEditText)) {
                    shoppingItemObject.setAisle(binding.isleEditText.getText().toString());
                }
                MainActivity.getDBHandler().handleShoppingItem(shoppingItemObject);
            }





        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            FoodObject obj = bundle.getParcelable("foodObject");
            if (obj != null) {
                binding.itemNameEditText.setText(obj.getName());
                if (obj.getQty() != null && obj.getUnit() != null) {
                    binding.foodItemQtyET.setText(obj.getQty());
                    binding.foodItemUnitSpinner.setSelection(foodItemUnitSpinnerAdaptor.getPosition(Enums.UNITS.getEnum(obj.getUnit())));
                }
                if (obj.getExpiry() != null) {
                    String str = obj.getExpiryAsString();
                    String[] strings = str.split("/");
                    binding.editTextDateDay.setText(strings[0]);
                    binding.editTextDateMonth.setText(strings[1]);
                    binding.editTextDateYear.setText(strings[2]);
                }
                if (obj.getIsle() != null) {
                    binding.isleEditText.setText(obj.getIsle());
                }
                if (obj.getCategory() != null) {
                    binding.foodItemCatSpinner.setSelection(foodItemCatSpinnerAdaptor.getPosition(obj.getCategory()));
                }
                if (obj.getPackaging() != null) {
                    binding.foodItemPackSpinner.setSelection(foodItemPackSpinnerAdaptor.getPosition(obj.getPackaging()));
                }
                if (obj.getNote() != null) {
                    binding.foodItemNotesEditText.setText(obj.getNote());
                }
            }
        }
    }
}