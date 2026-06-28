package sbnz.szrn.dto;

import sbnz.szrn.model.AssignmentResult;
import sbnz.szrn.model.Candidate;
import sbnz.szrn.model.ValidationMessage;

import java.util.List;

public class AllocationOutput {

    private List<Candidate> candidates;
    private List<AssignmentResult> assignmentResults;
    private List<ValidationMessage> validationMessages;
    private List<String> negativeExplanations;
    private int firedRules;
    private int totalAssignedHours;
    private double averagePreference;
    private String generatedDrl;

    public AllocationOutput(
            List<Candidate> candidates,
            List<AssignmentResult> assignmentResults,
            List<ValidationMessage> validationMessages,
            List<String> negativeExplanations,
            int firedRules,
            int totalAssignedHours,
            double averagePreference,
            String generatedDrl
    ) {
        this.candidates = candidates;
        this.assignmentResults = assignmentResults;
        this.validationMessages = validationMessages;
        this.negativeExplanations = negativeExplanations;
        this.firedRules = firedRules;
        this.totalAssignedHours = totalAssignedHours;
        this.averagePreference = averagePreference;
        this.generatedDrl = generatedDrl;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public List<AssignmentResult> getAssignmentResults() {
        return assignmentResults;
    }

    public List<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public List<String> getNegativeExplanations() {
        return negativeExplanations;
    }

    public int getFiredRules() {
        return firedRules;
    }

    public int getTotalAssignedHours() {
        return totalAssignedHours;
    }

    public double getAveragePreference() {
        return averagePreference;
    }

    public String getGeneratedDrl() {
        return generatedDrl;
    }
}
