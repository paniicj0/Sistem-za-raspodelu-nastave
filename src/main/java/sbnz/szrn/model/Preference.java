package sbnz.szrn.model;

public class Preference {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private int grade;

    public Preference() {
    }

    public Preference(Assistant assistant, Subject subject, int grade) {
        this.assistant = assistant;
        this.subject = subject;
        this.grade = grade;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }
}