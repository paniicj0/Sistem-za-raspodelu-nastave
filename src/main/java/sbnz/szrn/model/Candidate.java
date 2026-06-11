package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Candidate {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private int score;
    private String explanation;
    private boolean previousAssignmentApplied;
    private boolean specificRequestApplied;
    private boolean overloadPenaltyApplied;

    public Candidate(int id, Assistant assistant, Subject subject, int score, String explanation) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.score = score;
        this.explanation = explanation;
        this.previousAssignmentApplied = false;
        this.specificRequestApplied = false;
        this.overloadPenaltyApplied = false;
    }

    public void addScore(int value, String reason) {
        this.score += value;
        this.explanation = this.explanation + " " + reason;
    }
}