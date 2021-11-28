package com.enhanced.sustenance.ui.recipe_fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.R;
import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databases.JSON;
import com.enhanced.sustenance.databinding.FragmentAddRecipeBinding;
import com.enhanced.sustenance.ui.pantry_fragment.FoodObject;
import com.enhanced.sustenance.utils.Enums;
import com.enhanced.sustenance.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExpandedRecipeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "AddRecipeFragment";

private FragmentAddRecipeBinding binding;

private Bitmap bitmap;

    private final ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            try {
                assert data != null;
                InputStream stream = requireActivity().getContentResolver().openInputStream(data.getData());
                if (stream != null) {
                    //get the image
                    Bitmap bm = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(stream)).getBitmap();

                    //scale image
                    int maxImageSize = 500;
                    float ratio = Math.min((float) maxImageSize / bm.getWidth(), (float) maxImageSize / bm.getHeight());
                    int width = Math.round((float) ratio * bm.getWidth());
                    int height = Math.round((float) ratio * bm.getHeight());
                    bitmap = Bitmap.createScaledBitmap(bm, width, height, true);

                    //set image to imageview
                    binding.recipeImageView.setImageBitmap(null);
                    binding.recipeImageView.setImageBitmap(bitmap);
                    binding.recipeImageView.setBackground(null);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);

        binding.recipeSaveBtn.setOnClickListener(this);
        binding.addIngredientIB.setOnClickListener(this);
        binding.recipeImageView.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            RecipeObject obj = bundle.getParcelable("recipeObject");
            binding.recipeNameET.setText(obj.getName());
            binding.recipeDirectionsET.setText(obj.getDirections());
            binding.recipeDescriptionET.setText(obj.getDescription());
            binding.recipeNotesET.setText(obj.getNotes());

            if (obj.getIngredients() != null && binding.ingredientLinLay.getChildCount() < 2) {
                String[] ingredients = (obj.getIngredients()).split("///");
                for (String str : ingredients) {
                    String[] strings = str.split("---");
                    MaterialCardView card = (MaterialCardView) newIngredientWindow();

                    EditText nET = (EditText) card.findViewById(R.id.recipeIngredNameET);
                    EditText qET = (EditText) card.findViewById(R.id.recipeIngredQtyET);
                    Spinner spin = (Spinner) card.findViewById(R.id.recipeIngredUnitSpin);

                    if (strings[0] != null) {
                        nET.setText(strings[0]);
                    }
                    if (strings.length > 1 && strings[1] != null) {
                        qET.setText(strings[1]);
                    }

                    if (MainActivity.getFoodItemExpiryMap().containsKey(strings[0])) {
                        if (MainActivity.getFoodItemQtyMap().containsKey(strings[0]) && MainActivity.getFoodItemQtyMap().get(strings[0]) > 0) {
                            int i = MainActivity.getFoodItemExpiryMap().get(strings[0]);
                            if (i < 3) {
                                nET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_immediately, null));
                                qET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_immediately, null));
                            } else if (i < 7) {
                                nET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_asap, null));
                                qET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_asap, null));
                            } else if (i < 11) {
                                nET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_soon, null));
                                qET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_soon, null));
                            } else {
                                nET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_later, null));
                                qET.setTextColor(ResourcesCompat.getColor(getResources(), R.color.use_later, null));
                            }
                        }
                    }

                    for (int i = 0; i < spin.getCount(); i++){
                        if (strings.length > 2 && spin.getItemAtPosition(i).toString().equalsIgnoreCase(strings[2])){
                            spin.setSelection(i);
                        }
                    }

                    binding.ingredientLinLay.addView(card);
                }
            }
            binding.recipeFavouriteCB.setChecked(obj.getFavourite().equalsIgnoreCase("true"));

            MainActivity.getHandler().post(() -> {
                BitmapDrawable bmd = JSON.getImage(requireActivity(), obj.getName());
                if (bmd != null) {
                    Bitmap bm = bmd.getBitmap();
                    if (bm != null) {
                        requireActivity().runOnUiThread(() -> {
                            binding.recipeImageView.setImageBitmap(bm);
                            binding.recipeImageView.setBackground(null);
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.recipeSaveBtn) {
            if (!Utils.isEmpty(binding.recipeNameET)) {
                RecipeObject recipeObject = new RecipeObject(binding.recipeNameET.getText().toString());
                if (!Utils.isEmpty(binding.recipeDescriptionET)) {
                    recipeObject.setDescription(binding.recipeDescriptionET.getText().toString());
                }
                if (!Utils.isEmpty(binding.recipeDirectionsET)) {
                    recipeObject.setDirections(binding.recipeDirectionsET.getText().toString());
                }
                if (!Utils.isEmpty(binding.recipeNotesET)) {
                    recipeObject.setNotes(binding.recipeNotesET.getText().toString());
                }
                recipeObject.setIngredients(getIngredientsString());
                if (binding.recipeFavouriteCB.isChecked()) {
                    recipeObject.setFavourite("true");
                } else {
                    recipeObject.setFavourite("false");
                }

                if (bitmap != null) {
                    JSON.saveImage(getContext(), new BitmapDrawable(getResources(), bitmap), binding.recipeNameET.getText().toString());
                }

                MainActivity.getDBHandler().handleRecipeItem(recipeObject);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigateUp();
            }
        } else if (v == binding.addIngredientIB) {
            binding.ingredientLinLay.addView(newIngredientWindow());
        } else if (v == binding.recipeImageView) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            galleryResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        }
    }

    private MaterialCardView newIngredientWindow() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        MaterialCardView layout = (MaterialCardView) inflater.inflate(R.layout.recipe_new_ingredient_layout, null);

        ImageButton imageButton = layout.findViewById(R.id.recipeIngredDelIB);
        imageButton.setOnClickListener(v -> binding.ingredientLinLay.removeView(layout));

        ArrayAdapter<Enums.UNITS> spinnerAdaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, Enums.UNITS.values());
        Spinner spinner = layout.findViewById(R.id.recipeIngredUnitSpin);
        spinner.setAdapter(spinnerAdaptor);
        spinner.setSelection(0);

        return layout;
    }

    private String getIngredientsString() {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> arrayList = MainActivity.getDBHandler().getItemListByName(DBHandler.DATABASES.FOOD_ITEMS);
        for (int i = 0; i < binding.ingredientLinLay.getChildCount(); i++) {
            if (binding.ingredientLinLay.getChildAt(i) instanceof MaterialCardView) {
                View v = binding.ingredientLinLay.getChildAt(i);
                EditText nTV = (EditText) v.findViewById(R.id.recipeIngredNameET);
                if (!Utils.isEmpty(nTV)) {
                    EditText iTV = (EditText) v.findViewById(R.id.recipeIngredQtyET);
                    Spinner spin = (Spinner) v.findViewById(R.id.recipeIngredUnitSpin);
                    if (!Utils.isEmpty(nTV) && !Utils.isEmpty(iTV)) {
                        strings.add(nTV.getText().toString() + "---" + iTV.getText().toString() + "---" + Enums.UNITS.getEnum(spin.getSelectedItem().toString()));
                    }
                    if (!arrayList.contains(nTV.getText().toString())) {
                        FoodObject foodObject = new FoodObject(nTV.getText().toString());
                        MainActivity.getDBHandler().handleFoodItem(foodObject);
                    }
                }
            }
        }
        BootSequence.executeFoodObjects();
        StringBuilder returnStr = new StringBuilder();
        for (String str : strings) {
            returnStr.append(str).append("///");
        }

        return returnStr.toString();
    }

}