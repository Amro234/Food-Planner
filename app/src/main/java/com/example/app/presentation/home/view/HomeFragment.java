package com.example.app.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.databinding.FragmentHomeBinding;
import com.example.app.data.model.Category;
import com.example.app.data.model.Meal;
import com.example.app.presentation.home.presenter.HomeContract;
import com.example.app.presentation.home.presenter.HomePresenter;
import com.example.app.presentation.category.view.CategoryAdapter;
import com.example.app.presentation.category.view.MealListAdapter;
import com.example.app.presentation.Area.view.CountrySpinnerAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.app.data.model.Area;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private FragmentHomeBinding binding;
    private HomeContract.Presenter presenter;
    private CategoryAdapter categoryAdapter;
    private MealListAdapter recommendedAdapter;
    private Meal currentRandomMeal;
    private CountrySpinnerAdapter countrySpinnerAdapter;
    private Spinner countrySpinner;
    private EditText searchEditText;
    private final PublishSubject<String> searchSubject = PublishSubject.create();
    private CompositeDisposable searchDisposable = new CompositeDisposable();
    private String selectedArea = "Unknown";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = binding.searchEditText;
        countrySpinner = binding.countrySpinner;

        presenter = new HomePresenter(this, requireContext());
        setupRecyclerView();
        presenter.loadRandomMeal();
        presenter.loadCategories();
        presenter.loadRecommendedMeals();
        presenter.loadAreas();

        setupSearch();

        binding.mealOfDayCard.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealDetails action = com.example.app.presentation.home.view.HomeFragmentDirections
                        .actionHomeToMealDetails(currentRandomMeal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealList action = com.example.app.presentation.home.view.HomeFragmentDirections
                    .actionHomeToMealList(category.getStrCategory());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);

        recommendedAdapter = new MealListAdapter(new ArrayList<>(), new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealDetails action = com.example.app.presentation.home.view.HomeFragmentDirections
                        .actionHomeToMealDetails(meal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onAddToFavorite(Meal meal) {

            }
        });
        binding.recyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        binding.recyclerView.setAdapter(recommendedAdapter);
    }

    private void setupSearch() {
        searchDisposable.add(
                searchSubject
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(query -> {
                            if (!query.isEmpty()) {
                                navigateToSearch(query);
                            }
                        }));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSubject.onNext(s.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.searchIconButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            navigateToSearch(query);
        });
    }

    private void navigateToSearch(String query) {
        if (query != null && !query.isEmpty()) {
            HomeFragmentDirections.ActionHomeToSearch action = HomeFragmentDirections
                    .actionHomeToSearch()
                    .setQuery(query)
                    .setArea(selectedArea);
            Navigation.findNavController(binding.getRoot()).navigate(action);
            searchEditText.setText("");
        }
    }

    @Override
    public void showRandomMeal(Meal meal) {
        this.currentRandomMeal = meal;
        binding.mealOfDayTitle.setText(meal.getStrMeal());
        binding.mealOfDayCategory.setText(meal.getStrCategory());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(binding.mealOfDayImage);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (categoryAdapter != null) {
            categoryAdapter.updateCategories(categories);
        }
    }

    @Override
    public void showRecommendedMeals(List<Meal> meals) {
        if (recommendedAdapter != null) {
            recommendedAdapter.updateMeals(meals);
        }
    }

    @Override
    public void showLoading() {
        // TODO: Add progress bar to layout and show it here
    }

    @Override
    public void hideLoading() {
        // TODO: Add progress bar to layout and hide it here
    }

    @Override
    public void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAreas(List<Area> areas) {
        countrySpinnerAdapter = new CountrySpinnerAdapter(requireContext(), areas);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area area = areas.get(position);
                selectedArea = area.getStrArea();
                Toast.makeText(requireContext(), "Selected: " + selectedArea, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void showSearchResults(List<Meal> meals) {
        if (recommendedAdapter != null) {
            recommendedAdapter.updateMeals(meals);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
        searchDisposable.clear();
        binding = null;
    }
}
