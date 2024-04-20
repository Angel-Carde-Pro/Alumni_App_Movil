package com.gamesmindt.myapplication.Model;

public class MailRequest {
    private String name;

    private String to;

    private String from;

    private String subject;

    private String caseEmail;

    public MailRequest(String name, String to, String from, String subject, String caseEmail) {
        this.name = name;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.caseEmail = caseEmail;
    }

    public MailRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCaseEmail() {
        return caseEmail;
    }

    public void setCaseEmail(String caseEmail) {
        this.caseEmail = caseEmail;
    }
}
