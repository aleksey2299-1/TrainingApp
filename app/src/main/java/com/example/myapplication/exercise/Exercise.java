package com.example.myapplication.exercise;

public class Exercise {

    private String name, desc;
    private int reps, sets, timePerSet, relaxTimeBetweenSets;
    private long id, trainingId;

    public Exercise(long id, String name, String desc, int reps, int sets, int timePerSet, int relaxTimeBetweenSets, long trainingId) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.reps = reps;
        this.sets = sets;
        this.timePerSet = timePerSet;
        this.relaxTimeBetweenSets = relaxTimeBetweenSets;
        this.trainingId = trainingId;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getReps() {
        return reps;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
    public int getSets() {
        return sets;
    }
    public void setTimePerSet(int timePerSet) {
        this.timePerSet = timePerSet;
    }

    public int getTimePerSet() {
        return timePerSet;
    }

    public void setRelaxTimeBetweenSets(int relaxTimeBetweenSets) {
        this.relaxTimeBetweenSets = relaxTimeBetweenSets;
    }

    public int getRelaxTimeBetweenSets() {
        return relaxTimeBetweenSets;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }
    public long getTrainingId() {
        return trainingId;
    }


}
