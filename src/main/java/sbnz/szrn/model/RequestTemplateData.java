package sbnz.szrn.model;

public class RequestTemplateData {

    private String assistant;
    private String subject;
    private int priority;

    public RequestTemplateData() {}

    public RequestTemplateData(
            String assistant,
            String subject,
            int priority
    ) {
        this.assistant = assistant;
        this.subject = subject;
        this.priority = priority;
    }

    public String getAssistant() {
        return assistant;
    }

    public String getSubject() {
        return subject;
    }

    public int getPriority() {
        return priority;
    }
}