package sbnz.szrn.model;

public class SpecificRequest {

    private Assistant assistant;
    private Subject subject;
    private int priority;

    public SpecificRequest() {
    }

    public SpecificRequest(Assistant assistant, Subject subject, int priority) {
        this.assistant = assistant;
        this.subject = subject;
        this.priority = priority;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getPriority() {
        return priority;
    }
}