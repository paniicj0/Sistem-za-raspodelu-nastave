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
    private boolean relatedPreviousAssignmentApplied;
    private boolean overloadPenaltyApplied;
    private boolean subjectCountPenaltyApplied;
    private boolean hardLoadPenaltyApplied;
    private boolean assigned;

    public Candidate(int id, Assistant assistant, Subject subject, int score, String explanation) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.score = score;
        this.explanation = explanation;
        this.previousAssignmentApplied = false;
        this.specificRequestApplied = false;
        this.relatedPreviousAssignmentApplied = false;
        this.overloadPenaltyApplied = false;
        this.subjectCountPenaltyApplied = false;
        this.hardLoadPenaltyApplied = false;
        this.assigned = false;
    }

    public void addScore(int value, String reason) {
        this.score += value;
        this.explanation = this.explanation + " " + reason;
    }
}
