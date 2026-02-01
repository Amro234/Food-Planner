package com.example.app.presentation.person.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;
import android.widget.TextView;
import com.example.app.data.repository.UserRepository;
import com.example.app.data.repository.UserRepositoryImp;
import com.example.app.auth.AuthActivity;
import com.example.app.data.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;

public class person extends Fragment {

    private String mName;
    private String mEmail;
    private TextView nameTextView;
    private TextView emailTextView;
    private UserRepository userRepository;
    private FirebaseFirestore firestore;

    public person() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepositoryImp.getInstance(requireContext());
        firestore = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            mName = personArgs.fromBundle(getArguments()).getName();
            mEmail = personArgs.fromBundle(getArguments()).getEmail();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        nameTextView = view.findViewById(R.id.profile_name);
        emailTextView = view.findViewById(R.id.profile_email);

        if (userRepository.isGuestMode()) {
            nameTextView.setText("Guest");
            emailTextView.setText("N/A");
        } else {
            // First try to get from FirebaseAuth directly (fast and accurate for Google
            // sign-in)
            String displayName = userRepository.getCurrentUserDisplayName();
            String email = userRepository.getCurrentUserEmail();

            if (displayName != null && email != null) {
                nameTextView.setText(displayName);
                emailTextView.setText(email);
            }
            // Always fetch from Firestore to get updated/additional info
            fetchUserData();
        }

        view.findViewById(R.id.Logout_id).setOnClickListener(v -> {
            userRepository.logout();
            new com.example.app.Helper.SaveState(requireContext(), "on_boarding").setGuest(false);
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void fetchUserData() {
        String userId = userRepository.getCurrentUserId();
        if (userId != null) {
            firestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                mName = user.getUsername();
                                mEmail = user.getEmail();
                                nameTextView.setText(mName != null ? mName : "User");
                                emailTextView.setText(mEmail != null ? mEmail : "No Email");
                            }
                        }
                    });
        }
    }
}