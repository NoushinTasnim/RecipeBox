package com.example.quotepad.model;

import java.util.ArrayList;

public class MealPlanModel {
    String title, description, author, start_date, end_date, id, recipe;
    ArrayList<RecipeModel> recipes;

    public MealPlanModel(String title, String description, String author, String start_date, String end_date, String id) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.start_date = start_date;
        this.end_date = end_date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public ArrayList<RecipeModel> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<RecipeModel> recipes) {
        this.recipes = recipes;
    }
}
