package com.gamesmindt.myapplication.Model;

import java.util.List;

public class Survey {
    public Integer id;
    public String title;
    public String description;
    public List<Question> questions;
    public Boolean estado = false;

    public Survey(String title, String description, Boolean estado, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
