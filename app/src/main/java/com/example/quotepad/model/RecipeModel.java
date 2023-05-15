package com.example.quotepad.model;

public class RecipeModel {
    private String recipe_name, recipe_ing, recipe_inst, recipe_prep, time, type, author, id, publicity;

    RecipeModel(){

    }

    public RecipeModel(String recipe_name, String author) {
        this.recipe_name = recipe_name;
        this.author = author;
    }

    public RecipeModel(String recipe_name,  String recipe_ing, String recipe_inst, String recipe_prep, String time, String type, String author, String id) {
        this.recipe_name = recipe_name;
        this.recipe_ing = recipe_ing;
        this.recipe_inst = recipe_inst;
        this.recipe_prep = recipe_prep;
        this.time = time;
        this.type = type;
        this.author = author;
        this.id = id;
    }

    public RecipeModel(String recipe_name, String recipe_ing, String recipe_inst, String recipe_prep, String type, String time, String publicity) {
        this.recipe_name = recipe_name;
        this.recipe_ing = recipe_ing;
        this.recipe_inst = recipe_inst;
        this.recipe_prep = recipe_prep;
        this.time = time;
        this.type = type;
        this.publicity = publicity;
    }

    public String getRecipe_ing() {
        return recipe_ing;
    }

    public void setRecipe_ing(String recipe_ing) {
        this.recipe_ing = recipe_ing;
    }

    public String getRecipe_inst() {
        return recipe_inst;
    }

    public void setRecipe_inst(String recipe_inst) {
        this.recipe_inst = recipe_inst;
    }

    public String getRecipe_prep() {
        return recipe_prep;
    }

    public void setRecipe_prep(String recipe_prep) {
        this.recipe_prep = recipe_prep;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
