package com.example.myapplication.favoriteTrainings;

public class FavoriteTrainingByUser {

    private long id, userId, trainingId;

    public FavoriteTrainingByUser(long id, long userId, long trainingId){
        this.id = id;
        this.userId = userId;
        this.trainingId = trainingId;
    }
    public long getId() {
        return id;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getUserId() {
        return userId;
    }
    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }
    public long getTrainingId() {
        return trainingId;
    }
}
