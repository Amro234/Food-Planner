package com.example.app.presentation.plan.presenter;

import android.content.Context;

import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanPresenter implements PlanContract.Presenter {

    private final PlanContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public PlanPresenter(PlanContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void getPlannedMeals() {
        disposables.add(
                repository.getPlannedMeals()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (meals != null) {
                                        view.showPlannedMeals(meals);
                                    }
                                },
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void removeFromPlan(String mealId) {
        disposables.add(
                repository.removeFromPlan(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> view.showError("Removed from plan"),
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
