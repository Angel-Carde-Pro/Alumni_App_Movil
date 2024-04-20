package com.gamesmindt.myapplication.Model;

import java.util.HashMap;
import java.util.Map;

public class Answer {
    public Integer id;
    public Graduado graduado;
    public Carrera carrera;
    public Survey survey;
    public Map<Integer, String> answers;
    public String openAnswer;

    public Answer(Integer id, Graduado graduado, Carrera carrera, Survey survey, Map<Integer, String> answers, String openAnswer) {
        this.id = id;
        this.graduado = graduado;
        this.carrera = carrera;
        this.survey = survey;
        this.answers = answers;
        this.openAnswer = openAnswer;
    }

    public static Answer createWithoutGraduado(Integer id, Carrera carrera, Survey survey, Map<Integer, String> answers, String openAnswer) {
        return new Answer(id, null, carrera, survey, answers, openAnswer);
    }

    public void assignAnswerToQuestion(Integer questionId, String answer) {
        if (this.answers == null) {
            this.answers = new HashMap<>();
        }
        this.answers.put(questionId, answer);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Graduado getGraduado() {
        return graduado;
    }

    public void setGraduado(Graduado graduado) {
        this.graduado = graduado;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }
}

