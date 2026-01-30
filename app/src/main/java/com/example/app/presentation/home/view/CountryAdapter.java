package com.example.app.presentation.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.data.model.Area;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<Area> {

    public CountryAdapter(Context context, List<Area> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_country_dropdown, parent, false);
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.country_flag);
        TextView textViewName = convertView.findViewById(R.id.country_name);

        Area currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getStrArea());

            Glide.with(getContext())
                    .load(currentItem.getFlagUrl())
                    .placeholder(R.drawable.image_holder)
                    .error(R.drawable.image_holder)
                    .into(imageViewFlag);
        }

        return convertView;
    }
}
