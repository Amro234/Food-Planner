package com.example.app.data.model;

import java.util.List;

public class AreaResponse {
    private List<Area> meals; // API returns "meals" array even for areas

    public List<Area> getAreas() {
        return meals;
    }

    public void setAreas(List<Area> areas) {
        this.meals = areas;
    }
}
