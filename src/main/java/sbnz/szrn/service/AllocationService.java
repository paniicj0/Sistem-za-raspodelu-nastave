package sbnz.szrn.service;

import org.drools.template.ObjectDataCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;
import sbnz.szrn.Main;
import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.dto.AllocationOutput;
import sbnz.szrn.model.AssignmentResult;
import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.Candidate;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.PreviousAssignment;
import sbnz.szrn.model.RequestTemplateData;
import sbnz.szrn.model.SpecificRequest;
import sbnz.szrn.model.Subject;
import sbnz.szrn.model.ValidationMessage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AllocationService {

    public AllocationOutput allocate(AllocationInput input) {
        normalizeReferences(input);

        String generatedDrl = generateTemplateRules(input.getSpecificRequests());

        KieSession kieSession = createKieSession(generatedDrl);

        insertFacts(input, kieSession);

        int firedRules = kieSession.fireAllRules();

        List<Candidate> candidates = new ArrayList<>();
        List<AssignmentResult> assignmentResults = new ArrayList<>();
        List<ValidationMessage> validationMessages = new ArrayList<>();

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof Candidate candidate) {
                candidates.add(candidate);
            }

            if (fact instanceof AssignmentResult result) {
                assignmentResults.add(result);
            }

            if (fact instanceof ValidationMessage message) {
                validationMessages.add(message);
            }
        }

        int totalAssignedHours = calculateTotalAssignedHours(assignmentResults);
        double averagePreference = calculateAveragePreference(
                input.getPreferences(),
                assignmentResults
        );

        kieSession.dispose();

        return new AllocationOutput(
                candidates,
                assignmentResults,
                validationMessages,
                explainNegativeDecisions(candidates, assignmentResults),
                firedRules,
                totalAssignedHours,
                averagePreference,
                generatedDrl
        );
    }


    private void normalizeReferences(AllocationInput input) {
        Map<Integer, Assistant> assistantsById = input.getAssistants()
                .stream()
                .collect(Collectors.toMap(Assistant::getId, Function.identity()));

        Map<Integer, Subject> subjectsById = input.getSubjects()
                .stream()
                .collect(Collectors.toMap(Subject::getId, Function.identity()));

        for (Preference preference : input.getPreferences()) {
            preference.setAssistant(assistantsById.get(preference.getAssistant().getId()));
            preference.setSubject(subjectsById.get(preference.getSubject().getId()));
        }

        for (PreviousAssignment previousAssignment : input.getPreviousAssignments()) {
            previousAssignment.setAssistant(assistantsById.get(previousAssignment.getAssistant().getId()));
            previousAssignment.setSubject(subjectsById.get(previousAssignment.getSubject().getId()));
        }

        for (SpecificRequest specificRequest : input.getSpecificRequests()) {
            specificRequest.setAssistant(assistantsById.get(specificRequest.getAssistant().getId()));
            specificRequest.setSubject(subjectsById.get(specificRequest.getSubject().getId()));
        }
    }
    private String generateTemplateRules(List<SpecificRequest> specificRequests) {
        List<RequestTemplateData> templateData = new ArrayList<>();

        for (SpecificRequest request : specificRequests) {
            templateData.add(
                    new RequestTemplateData(
                            request.getAssistant().getName(),
                            request.getSubject().getName(),
                            request.getPriority(),
                            request.getRequestedHours()
                    )
            );
        }

        InputStream templateStream = Main.class.getResourceAsStream(
                "/templates/request-template.drt"
        );

        ObjectDataCompiler compiler = new ObjectDataCompiler();

        return compiler.compile(templateData, templateStream);
    }

    private KieSession createKieSession(String generatedDrl) {
        KieHelper kieHelper = new KieHelper();

        kieHelper.addResource(
                ResourceFactory.newClassPathResource("rules/assignment-rules.drl"),
                ResourceType.DRL
        );

        kieHelper.addContent(
                generatedDrl,
                ResourceType.DRL
        );

        return kieHelper.build().newKieSession();
    }

    private void insertFacts(AllocationInput input, KieSession kieSession) {
        input.getAssistants().forEach(kieSession::insert);
        input.getSubjects().forEach(kieSession::insert);
        input.getPreferences().forEach(kieSession::insert);
        input.getPreviousAssignments().forEach(kieSession::insert);
        input.getSpecificRequests().forEach(kieSession::insert);
    }

    private int calculateTotalAssignedHours(List<AssignmentResult> assignmentResults) {
        int total = 0;

        for (AssignmentResult result : assignmentResults) {
            total += result.getAssignedHours();
        }

        return total;
    }

    private double calculateAveragePreference(
            List<Preference> preferences,
            List<AssignmentResult> assignmentResults
    ) {
        if (assignmentResults.isEmpty()) {
            return 0;
        }

        int preferenceSum = 0;

        for (AssignmentResult result : assignmentResults) {
            preferenceSum += findPreferenceGrade(
                    preferences,
                    result
            );
        }

        return (double) preferenceSum / assignmentResults.size();
    }

    private int findPreferenceGrade(
            List<Preference> preferences,
            AssignmentResult result
    ) {
        for (Preference preference : preferences) {
            if (
                    preference.getAssistant() == result.getAssistant()
                            &&
                            preference.getSubject() == result.getSubject()
            ) {
                return preference.getGrade();
            }
        }

        return 0;
    }

    private List<String> explainNegativeDecisions(
            List<Candidate> candidates,
            List<AssignmentResult> assignmentResults
    ) {
        List<String> explanations = new ArrayList<>();

        for (Candidate candidate : candidates) {
            boolean selected = false;

            for (AssignmentResult result : assignmentResults) {
                if (
                        result.getAssistant() == candidate.getAssistant()
                                && result.getSubject() == candidate.getSubject()
                ) {
                    selected = true;
                    break;
                }
            }

            if (!selected) {
                explanations.add(buildNegativeDecisionExplanation(candidate, assignmentResults));
            }
        }

        return explanations;
    }

    private String buildNegativeDecisionExplanation(
            Candidate candidate,
            List<AssignmentResult> assignmentResults
    ) {
        AssignmentResult bestSubjectResult = findBestResultForSubject(
                candidate,
                assignmentResults
        );

        if (bestSubjectResult == null) {
            return candidate.getAssistant().getName()
                    + " nije dodeljen predmetu "
                    + candidate.getSubject().getName()
                    + " zato sto predmet nije rasporedjen. Razlozi kandidata: "
                    + candidate.getExplanation();
        }

        return candidate.getAssistant().getName()
                + " nije dodeljen predmetu "
                + candidate.getSubject().getName()
                + " zato sto je izabran "
                + bestSubjectResult.getAssistant().getName()
                + " sa score "
                + bestSubjectResult.getScore()
                + ", dok kandidat ima score "
                + candidate.getScore()
                + ". Razlozi kandidata: "
                + candidate.getExplanation();
    }

    private AssignmentResult findBestResultForSubject(
            Candidate candidate,
            List<AssignmentResult> assignmentResults
    ) {
        AssignmentResult bestResult = null;

        for (AssignmentResult result : assignmentResults) {
            if (result.getSubject() == candidate.getSubject()) {
                if (bestResult == null || result.getScore() > bestResult.getScore()) {
                    bestResult = result;
                }
            }
        }

        return bestResult;
    }
}
