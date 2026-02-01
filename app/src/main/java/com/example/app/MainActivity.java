package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.app.databinding.ActivityMainBinding;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.view.View;
import com.example.app.presentation.error.NoConnectionFragment;
import com.example.app.data.repository.MealRepositoryImp;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isNetworkAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showLoading(true);
        setupNavigation();
        registerConnectivityMonitor();
        checkConnectivity();
        performInitialSetup();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.mealDetailsFragment) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
                }
                updateErrorVisibility(destination.getId());
            });
        }
    }

    private void registerConnectivityMonitor() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                isNetworkAvailable = true;
                runOnUiThread(() -> showNoConnection(false));
            }

            @Override
            public void onLost(@NonNull Network network) {
                isNetworkAvailable = false;
                runOnUiThread(() -> showNoConnection(true));
            }
        });
    }

    private void checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager
                .getNetworkCapabilities(connectivityManager.getActiveNetwork());
        isNetworkAvailable = (capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
        showNoConnection(!isNetworkAvailable);
    }

    private void performInitialSetup() {
        MealRepositoryImp.getInstance(this).syncDataFromFirestore()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> showLoading(false),
                        throwable -> {
                            showLoading(false);
                            // Even if sync fails (e.g. guest), hide loading
                        });
    }

    public void showLoading(boolean show) {
        binding.loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showNoConnection(boolean show) {
        if (show) {
            updateErrorVisibility(-1); // Check current destination
        } else {
            binding.errorContainer.setVisibility(View.GONE);
            // Re-apply visibility based on current destination
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == R.id.mealDetailsFragment) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                }
            }
        }
    }

    private void updateErrorVisibility(int destinationId) {
        if (isNetworkAvailable) {
            binding.errorContainer.setVisibility(View.GONE);
            return;
        }

        if (destinationId == -1) {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                if (navController.getCurrentDestination() != null) {
                    destinationId = navController.getCurrentDestination().getId();
                }
            }
        }

        // Screens that work offline
        if (destinationId == R.id.fav_id || destinationId == R.id.plan_id || destinationId == R.id.prof_id) {
            binding.errorContainer.setVisibility(View.GONE);
        } else {
            binding.errorContainer.setVisibility(View.VISIBLE);
            if (getSupportFragmentManager().findFragmentById(R.id.error_container) == null) {
                NoConnectionFragment fragment = new NoConnectionFragment();
                fragment.setOnRetryListener(this::checkConnectivity);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.error_container, fragment)
                        .commitAllowingStateLoss();
            }
        }
    }
}
