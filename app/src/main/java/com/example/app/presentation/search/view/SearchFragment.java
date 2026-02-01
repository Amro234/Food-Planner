package com.example.app.presentation.search.view;

import static com.example.app.R.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.app.R;
import com.example.app.data.model.Meal;
import com.example.app.data.repository.UserRepositoryImp;
import com.example.app.databinding.SearchViewBinding;
import com.example.app.presentation.category.view.MealListAdapter;
import com.example.app.presentation.search.presenter.SearchContract;
import com.example.app.presentation.search.presenter.SearchPresenter;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.text.Editable;
import android.text.TextWatcher;

public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchViewBinding binding;
    private SearchContract.Presenter presenter;
    private MealListAdapter mealAdapter;
    private SearchCriteriaAdapter criteriaAdapter;
    private String initialQuery;
    private String initialArea;
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SearchFragmentArgs args = SearchFragmentArgs.fromBundle(getArguments());
            initialQuery = args.getQuery();
            initialArea = args.getArea();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = SearchViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SearchPresenter(this, requireContext());
        setupAdapters();
        setupRecyclerView();
        setupSearchLogic();

        if (initialQuery != null) {
            binding.SearchBar.getEditText().setText(initialQuery);
            if (initialArea != null) {
                binding.chip3.setChecked(true);
                presenter.getMealsByArea(initialArea);
            } else {
                presenter.searchMealsByName(initialQuery);
            }
        }
    }

    private void setupAdapters() {
        mealAdapter = new MealListAdapter(new ArrayList<>(), new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                SearchFragmentDirections.ActionSearchToDetails action = SearchFragmentDirections
                        .actionSearchToDetails(meal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onAddToFavorite(Meal meal) {
                if (UserRepositoryImp.getInstance(requireContext()).isGuestMode()) {
                    Toast.makeText(requireContext(), "Sign-in to add favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.addToFavorites(meal);
                    Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddToPlan(Meal meal) {
                if (UserRepositoryImp.getInstance(requireContext()).isGuestMode()) {
                    Toast.makeText(requireContext(), "Sign-in to add meals to plan!", Toast.LENGTH_SHORT).show();
                } else {
                    SearchFragmentDirections.ActionSearchToDetails action = SearchFragmentDirections
                            .actionSearchToDetails(meal.getIdMeal());
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            }
        });

        criteriaAdapter = new SearchCriteriaAdapter(criteria -> {
            switch (criteria.getType()) {
                case CATEGORY:
                    presenter.getMealsByCategory(criteria.getName());
                    break;
                case INGREDIENT:
                    presenter.getMealsByIngredient(criteria.getName());
                    break;
                case AREA:
                    presenter.getMealsByArea(criteria.getName());
                    break;
            }
        });
    }

    private void setupSearchLogic() {
        binding.SearchBar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(this::performSearch);

        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String query = binding.SearchBar.getEditText().getText().toString();
            if (!query.isEmpty()) {
                performSearch(query);
            }
        });
    }

    private void performSearch(String query) {
        int checkedId = binding.chipGroup.getCheckedChipId();
        if (checkedId == R.id.chip_1) {
            presenter.searchMealsByCategory(query);
        } else if (checkedId == R.id.chip_2) {
            presenter.searchMealsByIngredient(query);
        } else if (checkedId == R.id.chip_3) {
            presenter.searchMealsByArea(query);
        } else {
            presenter.searchMealsByName(query);
        }
    }

    private void setupRecyclerView() {
        binding.searchRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.searchRecyclerView.setAdapter(mealAdapter);
    }

    @Override
    public void showSearchResults(List<Meal> meals) {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.searchRecyclerView.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setAdapter(mealAdapter);
        mealAdapter.updateMeals(meals);
    }

    @Override
    public void showCategories(List<com.example.app.data.model.Category> categories) {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.searchRecyclerView.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setAdapter(criteriaAdapter);

        List<SearchCriteriaAdapter.SearchCriteria> criteriaList = new ArrayList<>();
        for (com.example.app.data.model.Category category : categories) {
            criteriaList.add(new SearchCriteriaAdapter.SearchCriteria(
                    category.getStrCategory(), category.getStrCategoryThumb(),
                    SearchCriteriaAdapter.SearchCriteria.Type.CATEGORY));
        }
        criteriaAdapter.updateList(criteriaList);
    }

    @Override
    public void showIngredients(List<com.example.app.data.model.Ingredient> ingredients) {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.searchRecyclerView.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setAdapter(criteriaAdapter);

        List<SearchCriteriaAdapter.SearchCriteria> criteriaList = new ArrayList<>();
        for (com.example.app.data.model.Ingredient ingredient : ingredients) {
            criteriaList.add(new SearchCriteriaAdapter.SearchCriteria(
                    ingredient.getStrIngredient(), ingredient.getImageUrl(),
                    SearchCriteriaAdapter.SearchCriteria.Type.INGREDIENT));
        }
        criteriaAdapter.updateList(criteriaList);
    }

    @Override
    public void showAreas(List<com.example.app.data.model.Area> areas) {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.searchRecyclerView.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setAdapter(criteriaAdapter);

        List<SearchCriteriaAdapter.SearchCriteria> criteriaList = new ArrayList<>();
        for (com.example.app.data.model.Area area : areas) {
            criteriaList.add(new SearchCriteriaAdapter.SearchCriteria(
                    area.getStrArea(), area.getFlagUrl(), SearchCriteriaAdapter.SearchCriteria.Type.AREA));
        }
        criteriaAdapter.updateList(criteriaList);
    }

    @Override
    public void showLoading() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.searchProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        binding.searchRecyclerView.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}
