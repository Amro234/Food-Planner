package com.example.app.presentation.Area.view;

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
import com.example.app.data.model.Area;
import com.example.app.R;

import java.util.List;

public class CountrySpinnerAdapter extends ArrayAdapter<Area> {

    private Context context;
    private List<Area> countries;

    public CountrySpinnerAdapter(@NonNull Context context, @NonNull List<Area> countries) {
        super(context, 0, countries);
        this.context = context;
        this.countries = countries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createDropDownView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_country_dropdown, parent, false);
        }

        ImageView flag = convertView.findViewById(R.id.country_flag);
        TextView name = convertView.findViewById(R.id.country_name);

        Area area = getItem(position);

        if (area != null) {
            name.setText(area.getStrArea());

            Glide.with(context)
                    .load(area.getFlagUrl())
                    .placeholder(R.drawable.image_holder)
                    .error(R.drawable.image_holder)
                    .into(flag);
        }

        return convertView;
    }

    private View createDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_country_dropdown, parent, false);
            holder = new ViewHolder();
            holder.flagImageView = convertView.findViewById(R.id.country_flag);
            holder.countryNameTextView = convertView.findViewById(R.id.country_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Area area = getItem(position);

        if (area != null) {
            holder.countryNameTextView.setText(area.getStrArea());

            // Load flag using Glide
            Glide.with(context)
                    .load(area.getFlagUrl())
                    .placeholder(R.drawable.image_holder)
                    .error(R.drawable.image_holder)
                    .into(holder.flagImageView);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView flagImageView;
        TextView countryNameTextView;
    }
}
