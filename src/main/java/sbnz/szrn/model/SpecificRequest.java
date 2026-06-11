package sbnz.szrn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificRequest {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private Integer requestedHours;
    private int priority;

    public SpecificRequest(int id, Assistant assistant, Subject subject, Integer requestedHours, int priority) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.requestedHours = requestedHours;
        this.priority = priority;
    }

}