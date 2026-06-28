package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssignmentResult {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private int assignedHours;
    private String explanation;
    private int score;

    public AssignmentResult(
            int id,
            Assistant assistant,
            Subject subject,
            int assignedHours,
            int score,
            String explanation
    ) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.assignedHours = assignedHours;
        this.score = score;
        this.explanation = explanation;
    }
}