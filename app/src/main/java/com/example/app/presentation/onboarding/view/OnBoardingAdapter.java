package com.example.app.presentation.onboarding.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.app.R;

public class OnBoardingAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public OnBoardingAdapter(Context context) {
        this.context = context;
    }

    // Data for the slides
    private int[] images = {
            R.drawable.chief_photo,
            R.drawable.calender_photo,
            R.drawable.vegtables_photo
    };

    private int[] titles = {
            R.string.title1,
            R.string.title2,
            R.string.title3
    };

    private int[] descriptions = {
            R.string.desc_title1,
            R.string.desc_title2,
            R.string.desc_title3
    };

    private int[] backgrounds = {
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider, container, false);

        ConstraintLayout layout = (ConstraintLayout) view;
        ImageView slideImageView = view.findViewById(R.id.slide_img);
        TextView slideTitle = view.findViewById(R.id.slide_title);
        TextView slideDescription = view.findViewById(R.id.slide_subtitle);

        layout.setBackgroundResource(backgrounds[position]);
        slideImageView.setImageResource(images[position]);
        slideTitle.setText(titles[position]);
        slideDescription.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
