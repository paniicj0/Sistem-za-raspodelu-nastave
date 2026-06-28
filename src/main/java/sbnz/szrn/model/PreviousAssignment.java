package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PreviousAssignment {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private int assignedHours;

    public PreviousAssignment(int id, Assistant assistant, Subject subject, int assignedHours) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.assignedHours = assignedHours;
    }

}
