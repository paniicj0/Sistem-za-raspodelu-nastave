package sbnz.szrn.model;

public class PreviousAssignment {

    private Assistant assistant;
    private Subject subject;

    public PreviousAssignment() {
    }

    public PreviousAssignment(Assistant assistant, Subject subject) {
        this.assistant = assistant;
        this.subject = subject;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Subject getSubject() {
        return subject;
    }
}
