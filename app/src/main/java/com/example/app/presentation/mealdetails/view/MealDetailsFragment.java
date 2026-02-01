package com.example.app.presentation.mealdetails.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.data.model.Ingredient;
import com.example.app.databinding.FragmentMealdetailsBinding;
import com.example.app.data.model.Meal;
import com.example.app.presentation.mealdetails.presenter.MealDetailsContract;
import com.example.app.presentation.mealdetails.presenter.MealDetailsPresenter;
import com.example.app.presentation.ingerdient.view.IngredientsAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.widget.CalendarView;
import android.widget.Button;
import com.example.app.data.repository.UserRepositoryImp;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {

    private FragmentMealdetailsBinding binding;
    private MealDetailsContract.Presenter presenter;
    private IngredientsAdapter ingredientsAdapter;
    private String mealId;
    private Meal currentMeal;
    private String selectedDateString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentMealdetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientsAdapter = new IngredientsAdapter(new ArrayList<>());
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        binding.ingredientsRecyclerView
                .setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter = new MealDetailsPresenter(this, requireContext());
        if (mealId != null) {
            presenter.getMealById(mealId);

        }
    }

    @Override
    public void showIngredients(java.util.List<Ingredient> ingredients) {
        ingredientsAdapter.setList(ingredients);
    }

    @Override
    public void showMealDetails(Meal meal) {
        this.currentMeal = meal;
        binding.mealName.setText(meal.getStrMeal());
        binding.mealCategory.setText(meal.getStrCategory());
        binding.mealArea.setText(meal.getStrArea());
        binding.mealInstructions.setText(meal.getStrInstructions());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(binding.mealImage);

        binding.addToFavButton.setOnClickListener(v -> {
            if (UserRepositoryImp.getInstance(requireContext()).isGuestMode()) {
                Toast.makeText(requireContext(), "Sign-in to add favorites!", Toast.LENGTH_SHORT).show();
            } else {
                presenter.addToFavorites(meal);
            }
        });

        binding.btnAddToPlan.setOnClickListener(v -> {
            if (UserRepositoryImp.getInstance(requireContext()).isGuestMode()) {
                Toast.makeText(requireContext(), "Sign-in to add to plan!", Toast.LENGTH_SHORT).show();
            } else {
                showCustomCalendarDialog();
            }
        });

        if (meal.getStrYoutube() != null && !meal.getStrYoutube().isEmpty()) {
            String videoId = extractVideoId(meal.getStrYoutube());

            getLifecycle().addObserver(binding.youtubePlayerView);

            binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                    youtubePlayer.cueVideo(videoId, 0);
                }
            });
        } else {
            binding.youtubePlayerView.setVisibility(View.GONE);
        }
    }

    private void showCustomCalendarDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_calendar, null);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button btnConfirm = view.findViewById(R.id.btn_confirm_date);

        Calendar calendar = Calendar.getInstance();

        long today = calendar.getTimeInMillis();
        calendarView.setMinDate(today);

        // Adjusting setMaxDate to allow 30 days ahead as requested for better UX
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        long endOfPeriod = calendar.getTimeInMillis();
        calendarView.setMaxDate(endOfPeriod);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        calendar = Calendar.getInstance();
        selectedDateString = sdf.format(calendar.getTime());

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar clickedDate = Calendar.getInstance();
            clickedDate.set(year, month, dayOfMonth);
            selectedDateString = sdf.format(clickedDate.getTime());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            presenter.addToPlan(currentMeal, selectedDateString);
            dialog.dismiss();
        });

        dialog.show();
    }

    private String extractVideoId(String youtubeUrl) {
        if (youtubeUrl != null && youtubeUrl.contains("v=")) {
            return youtubeUrl.split("v=")[1].split("&")[0];
        }
        return "";
    }

    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}
