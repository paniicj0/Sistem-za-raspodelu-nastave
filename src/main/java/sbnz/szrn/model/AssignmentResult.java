package sbnz.szrn.model;

public class AssignmentResult {

    private Assistant assistant;
    private Subject subject;
    private int assignedHours;
    private String explanation;

    public AssignmentResult() {
    }

    public AssignmentResult(Assistant assistant, Subject subject, int assignedHours, String explanation) {
        this.assistant = assistant;
        this.subject = subject;
        this.assignedHours = assignedHours;
        this.explanation = explanation;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getAssignedHours() {
        return assignedHours;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public String toString() {
        return "Predmet '" + subject.getName() + "' dodeljen asistentu " + assistant.getName()
                + " (" + assignedHours + " časova). Razlog: " + explanation;
    }
}