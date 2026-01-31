package com.example.app.presentation.error;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app.R;

public class NoConnectionFragment extends Fragment {

    public interface OnRetryListener {
        void onRetry();
    }

    private OnRetryListener retryListener;

    public void setOnRetryListener(OnRetryListener listener) {
        this.retryListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_connection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.retryButton).setOnClickListener(v -> {
            if (retryListener != null) {
                retryListener.onRetry();
            }
        });
    }
}
