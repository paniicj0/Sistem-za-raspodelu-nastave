package sbnz.szrn.dto;

import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.PreviousAssignment;
import sbnz.szrn.model.SpecificRequest;
import sbnz.szrn.model.Subject;
import sbnz.szrn.model.SubjectRelation;

import java.util.ArrayList;
import java.util.List;

public class AllocationInput {

    private List<Assistant> assistants = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private List<Preference> preferences = new ArrayList<>();
    private List<PreviousAssignment> previousAssignments = new ArrayList<>();
    private List<SubjectRelation> subjectRelations = new ArrayList<>();
    private List<SpecificRequest> specificRequests = new ArrayList<>();

    public AllocationInput() {
    }

    public AllocationInput(
            List<Assistant> assistants,
            List<Subject> subjects,
            List<Preference> preferences,
            List<PreviousAssignment> previousAssignments,
            List<SpecificRequest> specificRequests
    ) {
        this.assistants = assistants;
        this.subjects = subjects;
        this.preferences = preferences;
        this.previousAssignments = previousAssignments;
        this.specificRequests = specificRequests;
    }

    public AllocationInput(
            List<Assistant> assistants,
            List<Subject> subjects,
            List<Preference> preferences,
            List<PreviousAssignment> previousAssignments,
            List<SubjectRelation> subjectRelations,
            List<SpecificRequest> specificRequests
    ) {
        this.assistants = assistants;
        this.subjects = subjects;
        this.preferences = preferences;
        this.previousAssignments = previousAssignments;
        this.subjectRelations = subjectRelations;
        this.specificRequests = specificRequests;
    }

    public List<Assistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<Assistant> assistants) {
        this.assistants = assistants;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public List<PreviousAssignment> getPreviousAssignments() {
        return previousAssignments;
    }

    public void setPreviousAssignments(List<PreviousAssignment> previousAssignments) {
        this.previousAssignments = previousAssignments;
    }

    public List<SubjectRelation> getSubjectRelations() {
        return subjectRelations;
    }

    public void setSubjectRelations(List<SubjectRelation> subjectRelations) {
        this.subjectRelations = subjectRelations;
    }

    public List<SpecificRequest> getSpecificRequests() {
        return specificRequests;
    }

    public void setSpecificRequests(List<SpecificRequest> specificRequests) {
        this.specificRequests = specificRequests;
    }
}
