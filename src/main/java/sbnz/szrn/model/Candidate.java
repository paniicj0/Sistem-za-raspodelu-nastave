package sbnz.szrn.model;

public class Candidate {

    private Assistant assistant;
    private Subject subject;
    private int score;
    private String explanation;

    public Candidate() {
    }

    public Candidate(Assistant assistant, Subject subject, int score, String explanation) {
        this.assistant = assistant;
        this.subject = subject;
        this.score = score;
        this.explanation = explanation;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getScore() {
        return score;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
