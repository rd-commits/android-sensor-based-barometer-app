package com.example.mymediaplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private TextView pressureText;
    private TextView forecastText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        pressureText = view.findViewById(R.id.pressureText);
        forecastText = view.findViewById(R.id.forecastText);

        return view;
    }

    public void updateData(float pressure, String forecast) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            pressureText.setText(String.format("%.2f hPa", pressure));
            forecastText.setText(forecast);
        });
    }
}
