package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

            binding.bottomNavigation.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.logout_id) {
                    // Handle Logout
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
                    new com.example.app.Helper.SaveState(this, "on_boarding").setGuest(false);
                    android.content.Intent intent = new android.content.Intent(this,
                            com.example.app.auth.AuthActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item);
            });
        }
    }
}
