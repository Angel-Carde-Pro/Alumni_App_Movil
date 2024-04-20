package com.gamesmindt.myapplication.Model;

import java.util.Map;

public class AnswerSearchDTO {
    private Integer id;
    private String graduadoEmail;
    private String carreraNombre;
    private String surveyTitle;
    private Map<Integer, String> questionResponses;
    private String openAnswer;

    public AnswerSearchDTO() {
    }

    public AnswerSearchDTO(Integer id, String graduadoEmail, String carreraNombre, String surveyTitle, Map<Integer, String> questionResponses, String openAnswer) {
        this.id = id;
        this.graduadoEmail = graduadoEmail;
        this.carreraNombre = carreraNombre;
        this.surveyTitle = surveyTitle;
        this.questionResponses = questionResponses;
        this.openAnswer = openAnswer;
    }

    public void assignResponseToQuestion(Integer questionId, String response) {
        this.questionResponses.put(questionId, response);
    }

    public void setQuestionResponses(Map<Integer, String> questionResponses) {
        this.questionResponses = questionResponses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGraduadoEmail() {
        return graduadoEmail;
    }

    public void setGraduadoEmail(String graduadoEmail) {
        this.graduadoEmail = graduadoEmail;
    }

    public String getCarreraNombre() {
        return carreraNombre;
    }

    public void setCarreraNombre(String carreraNombre) {
        this.carreraNombre = carreraNombre;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public Map<Integer, String> getQuestionResponses() {
        return questionResponses;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }
}
