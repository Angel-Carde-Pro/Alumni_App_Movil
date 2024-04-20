package com.gamesmindt.myapplication.Model;
import java.util.List;

public class Question {
    public Integer id;
    public String text;
    public QuestionType type;
    public Survey survey;
    public List<String> options;

    public Question(String text, QuestionType type, List<String> options) {
        this.text = text;
        this.type = type;
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}

