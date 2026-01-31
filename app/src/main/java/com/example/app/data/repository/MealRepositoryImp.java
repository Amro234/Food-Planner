package com.example.app.data.repository;

import android.content.Context;

import com.example.app.Database.MealEntity;
import com.example.app.data.datasource.local.MealLocalDataSource;
import com.example.app.data.datasource.local.MealLocalDataSourceImp;
import com.example.app.data.datasource.remote.MealRemoteDataSource;
import com.example.app.data.datasource.remote.MealRemoteDataSourceImp;
import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.Meal;
import com.example.app.data.model.MealResponse;
import com.example.app.data.repository.UserRepository;
import com.example.app.data.repository.UserRepositoryImp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImp implements MealRepository {

    private static MealRepositoryImp instance = null;
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private final UserRepository userRepository;
    private final FirebaseFirestore firestore;

    private MealRepositoryImp(Context context) {
        this.remoteDataSource = new MealRemoteDataSourceImp();
        this.localDataSource = new MealLocalDataSourceImp(context);
        this.userRepository = UserRepositoryImp.getInstance(context);
        this.firestore = FirebaseFirestore.getInstance();
    }

    public static MealRepositoryImp getInstance(Context context) {
        if (instance == null) {
            instance = new MealRepositoryImp(context);
        }
        return instance;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<MealResponse> getMealsByCategory(String category) {
        return remoteDataSource.getMealsByCategory(category);
    }

    @Override
    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return remoteDataSource.getMealsByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealsByArea(String area) {
        return remoteDataSource.getMealsByArea(area);
    }

    @Override
    public Single<MealResponse> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId);
    }

    @Override
    public Single<com.example.app.data.model.AreaResponse> getAreas() {
        return remoteDataSource.getAreas();
    }

    @Override
    public Single<com.example.app.data.model.IngredientResponse> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    @Override
    public Single<MealResponse> searchMeals(String query) {
        return remoteDataSource.searchMeals(query);
    }

    @Override
    public Flowable<List<MealEntity>> getFavoriteMeals() {
        String userId = userRepository.getCurrentUserId();
        return localDataSource.getAllFavoriteMeals(userId != null ? userId : "guest");
    }

    @Override
    public Completable addToFavorites(Meal meal) {
        MealEntity entity = convertMealToEntity(meal);
        entity.setFavorite(true);
        String userId = userRepository.getCurrentUserId();
        entity.setUserId(userId != null ? userId : "guest");

        return localDataSource.insertMeal(entity)
                .andThen(Completable.create(emitter -> {
                    if (userRepository.isUserLoggedIn()) {
                        uploadToFirestore(entity, "favorites", emitter);
                    } else {
                        emitter.onComplete();
                    }
                }));
    }

    @Override
    public Completable removeFromFavorites(String mealId) {
        return localDataSource.updateFavoriteStatus(mealId, false)
                .andThen(Completable.create(emitter -> {
                    if (userRepository.isUserLoggedIn()) {
                        removeFromFirestore(mealId, "favorites", emitter);
                    } else {
                        emitter.onComplete();
                    }
                }));
    }

    @Override
    public Flowable<List<MealEntity>> getPlannedMeals() {
        String userId = userRepository.getCurrentUserId();
        return localDataSource.getPlannedMeals(userId != null ? userId : "guest");
    }

    @Override
    public Completable addToPlan(Meal meal, String day) {
        MealEntity entity = convertMealToEntity(meal);
        entity.setPlanned(true);
        entity.setPlannedDay(day);
        String userId = userRepository.getCurrentUserId();
        entity.setUserId(userId != null ? userId : "guest");

        return localDataSource.insertMeal(entity)
                .andThen(Completable.create(emitter -> {
                    if (userRepository.isUserLoggedIn()) {
                        uploadToFirestore(entity, "planned", emitter);
                    } else {
                        emitter.onComplete();
                    }
                }));
    }

    @Override
    public Completable removeFromPlan(String mealId) {
        return localDataSource.updatePlannedStatus(mealId, false, null)
                .andThen(Completable.create(emitter -> {
                    if (userRepository.isUserLoggedIn()) {
                        removeFromFirestore(mealId, "planned", emitter);
                    } else {
                        emitter.onComplete();
                    }
                }));
    }

    // FireStore sync method
    private void uploadToFirestore(MealEntity entity, String collection, CompletableEmitter emitter) {
        String userId = userRepository.getCurrentUserId();
        firestore.collection("users").document(userId)
                .collection(collection).document(entity.getIdMeal())
                .set(entity)
                .addOnSuccessListener(aVoid -> emitter.onComplete())
                .addOnFailureListener(emitter::onError);
    }

    private void removeFromFirestore(String mealId, String collection, CompletableEmitter emitter) {
        String userId = userRepository.getCurrentUserId();
        firestore.collection("users").document(userId)
                .collection(collection).document(mealId)
                .delete()
                .addOnSuccessListener(aVoid -> emitter.onComplete())
                .addOnFailureListener(emitter::onError);
    }

    // FireStore sync back user (Favourite meals) data if user open from another
    // device
    @Override
    public Completable syncFromFirestore() {
        String userId = userRepository.getCurrentUserId();
        if (userId == null)
            return Completable.complete();

        return Completable.create(emitter -> {
            firestore.collection("users").document(userId).collection("favorites")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<MealEntity> favorites = queryDocumentSnapshots.toObjects(MealEntity.class);
                        for (MealEntity meal : favorites) {
                            meal.setUserId(userId);
                            meal.setFavorite(true);
                        }
                        localDataSource.insertMeals(favorites)
                                .subscribe(emitter::onComplete, emitter::onError);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    private MealEntity convertMealToEntity(Meal meal) {
        MealEntity entity = new MealEntity();
        entity.setIdMeal(meal.getIdMeal());
        entity.setStrMeal(meal.getStrMeal());
        entity.setStrCategory(meal.getStrCategory());
        entity.setStrArea(meal.getStrArea());
        entity.setStrInstructions(meal.getStrInstructions());
        entity.setStrMealThumb(meal.getStrMealThumb());
        entity.setStrTags(meal.getStrTags());
        entity.setStrYoutube(meal.getStrYoutube());

        entity.setStrIngredient1(meal.getStrIngredient1());
        entity.setStrIngredient2(meal.getStrIngredient2());
        entity.setStrIngredient3(meal.getStrIngredient3());
        entity.setStrIngredient4(meal.getStrIngredient4());
        entity.setStrIngredient5(meal.getStrIngredient5());
        entity.setStrIngredient6(meal.getStrIngredient6());
        entity.setStrIngredient7(meal.getStrIngredient7());
        entity.setStrIngredient8(meal.getStrIngredient8());
        entity.setStrIngredient9(meal.getStrIngredient9());
        entity.setStrIngredient10(meal.getStrIngredient10());
        entity.setStrIngredient11(meal.getStrIngredient11());
        entity.setStrIngredient12(meal.getStrIngredient12());
        entity.setStrIngredient13(meal.getStrIngredient13());
        entity.setStrIngredient14(meal.getStrIngredient14());
        entity.setStrIngredient15(meal.getStrIngredient15());
        entity.setStrIngredient16(meal.getStrIngredient16());
        entity.setStrIngredient17(meal.getStrIngredient17());
        entity.setStrIngredient18(meal.getStrIngredient18());
        entity.setStrIngredient19(meal.getStrIngredient19());
        entity.setStrIngredient20(meal.getStrIngredient20());

        entity.setStrMeasure1(meal.getStrMeasure1());
        entity.setStrMeasure2(meal.getStrMeasure2());
        entity.setStrMeasure3(meal.getStrMeasure3());
        entity.setStrMeasure4(meal.getStrMeasure4());
        entity.setStrMeasure5(meal.getStrMeasure5());
        entity.setStrMeasure6(meal.getStrMeasure6());
        entity.setStrMeasure7(meal.getStrMeasure7());
        entity.setStrMeasure8(meal.getStrMeasure8());
        entity.setStrMeasure9(meal.getStrMeasure9());
        entity.setStrMeasure10(meal.getStrMeasure10());
        entity.setStrMeasure11(meal.getStrMeasure11());
        entity.setStrMeasure12(meal.getStrMeasure12());
        entity.setStrMeasure13(meal.getStrMeasure13());
        entity.setStrMeasure14(meal.getStrMeasure14());
        entity.setStrMeasure15(meal.getStrMeasure15());
        entity.setStrMeasure16(meal.getStrMeasure16());
        entity.setStrMeasure17(meal.getStrMeasure17());
        entity.setStrMeasure18(meal.getStrMeasure18());
        entity.setStrMeasure19(meal.getStrMeasure19());
        entity.setStrMeasure20(meal.getStrMeasure20());

        entity.setStrSource(meal.getStrSource());
        entity.setStrImageSource(meal.getStrImageSource());

        return entity;
    }
}
