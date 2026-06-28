package sbnz.szrn.report;

import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.dto.AllocationOutput;
import sbnz.szrn.model.AssignmentResult;
import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.Candidate;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.PreviousAssignment;
import sbnz.szrn.model.SpecificRequest;
import sbnz.szrn.model.Subject;
import sbnz.szrn.model.ValidationMessage;

public class ConsoleReportPrinter {

    private ConsoleReportPrinter() {
    }

    public static void print(AllocationInput input, AllocationOutput output) {
        printAssistants(input);
        printSubjects(input);
        printPreferences(input);
        printPreviousAssignments(input);
        printSpecificRequests(input);

        System.out.println();
        System.out.println("GENERISANA TEMPLATE PRAVILA");
        System.out.println(output.getGeneratedDrl());

        printCandidates(output);
        printAssignmentResults(output);
        printAssistantLoads(input);
        printValidation(output);
        printStatistics(output);

        explainAssignment(
                output,
                "Milan Segedinac",
                "Osnove racunarstva i programiranje"
        );
    }

    private static void printAssistants(AllocationInput input) {
        System.out.println();
        System.out.println("ASISTENTI");

        for (Assistant assistant : input.getAssistants()) {
            System.out.println(
                    assistant.getId()
                            + " | "
                            + assistant.getName()
                            + " | target load: "
                            + assistant.getTargetLoad()
                            + " | winter load: "
                            + assistant.getWinterLoad()
                            + " | summer load: "
                            + assistant.getSummerLoad()
            );
        }
    }

    private static void printSubjects(AllocationInput input) {
        System.out.println();
        System.out.println("PREDMETI");

        for (Subject subject : input.getSubjects()) {
            System.out.println(
                    subject.getId()
                            + " | "
                            + subject.getName()
                            + " | semester: "
                            + subject.getSemester()
                            + " | groups: "
                            + subject.getGroups()
                            + " | hours per group: "
                            + subject.getHoursPerGroup()
                            + " | total hours: "
                            + subject.getTotalHours()
            );
        }
    }

    private static void printPreferences(AllocationInput input) {
        System.out.println();
        System.out.println("PREFERENCE");

        for (Preference preference : input.getPreferences()) {
            System.out.println(
                    preference.getId()
                            + " | "
                            + preference.getAssistant().getName()
                            + " -> "
                            + preference.getSubject().getName()
                            + " | grade: "
                            + preference.getGrade()
            );
        }
    }

    private static void printPreviousAssignments(AllocationInput input) {
        System.out.println();
        System.out.println("PRETHODNA RASPODELA");

        for (PreviousAssignment assignment : input.getPreviousAssignments()) {
            System.out.println(
                    assignment.getId()
                            + " | "
                            + assignment.getAssistant().getName()
                            + " -> "
                            + assignment.getSubject().getName()
                            + " | hours: "
                            + assignment.getAssignedHours()
            );
        }
    }

    private static void printSpecificRequests(AllocationInput input) {
        System.out.println();
        System.out.println("SPECIFICNI ZAHTEVI");

        for (SpecificRequest request : input.getSpecificRequests()) {
            String requestedHours = request.getRequestedHours() == null
                    ? "nije definisano"
                    : request.getRequestedHours().toString();

            System.out.println(
                    request.getId()
                            + " | "
                            + request.getAssistant().getName()
                            + " -> "
                            + request.getSubject().getName()
                            + " | requested hours: "
                            + requestedHours
                            + " | priority: "
                            + request.getPriority()
            );
        }
    }

    private static void printCandidates(AllocationOutput output) {
        System.out.println();
        System.out.println("KANDIDATI");

        for (Candidate candidate : output.getCandidates()) {
            System.out.println(
                    candidate.getAssistant().getName()
                            + " -> "
                            + candidate.getSubject().getName()
                            + " | score: "
                            + candidate.getScore()
                            + " | "
                            + candidate.getExplanation()
            );
        }
    }

    private static void printAssignmentResults(AllocationOutput output) {
        System.out.println();
        System.out.println("KONACNA RASPODELA");

        for (AssignmentResult result : output.getAssignmentResults()) {
            System.out.println(
                    result.getAssistant().getName()
                            + " -> "
                            + result.getSubject().getName()
                            + " | hours: "
                            + result.getAssignedHours()
                            + " | score: "
                            + result.getScore()
                            + " | "
                            + result.getExplanation()
            );
        }
    }

    private static void printAssistantLoads(AllocationInput input) {
        System.out.println();
        System.out.println("OPTERECENJE ASISTENATA NAKON RASPODELE");

        for (Assistant assistant : input.getAssistants()) {
            System.out.println(
                    assistant.getName()
                            + " | winter: "
                            + assistant.getWinterLoad()
                            + " | summer: "
                            + assistant.getSummerLoad()
                            + " | total: "
                            + assistant.getTotalLoad()
                            + " | target: "
                            + assistant.getTargetLoad()
            );
        }
    }

    private static void printValidation(AllocationOutput output) {
        System.out.println();
        System.out.println("VALIDACIJA");

        if (output.getValidationMessages().isEmpty()) {
            System.out.println("Nema validacionih gresaka.");
            return;
        }

        for (ValidationMessage message : output.getValidationMessages()) {
            System.out.println(
                    message.getLevel()
                            + " | "
                            + message.getMessage()
            );
        }
    }

    private static void printStatistics(AllocationOutput output) {
        System.out.println();
        System.out.println("STATISTIKA RASPODELE");

        System.out.println("Broj aktiviranih pravila: " + output.getFiredRules());
        System.out.println("Broj dodela: " + output.getAssignmentResults().size());
        System.out.println("Ukupno dodeljenih casova: " + output.getTotalAssignedHours());
        System.out.println("Prosecna preferenca: " + output.getAveragePreference());
    }

    private static void explainAssignment(
            AllocationOutput output,
            String assistantName,
            String subjectName
    ) {
        System.out.println();
        System.out.println("OBJASNJENJE DODELE");

        for (AssignmentResult result : output.getAssignmentResults()) {
            boolean sameAssistant = result.getAssistant()
                    .getName()
                    .equals(assistantName);

            boolean sameSubject = result.getSubject()
                    .getName()
                    .equals(subjectName);

            if (sameAssistant && sameSubject) {
                System.out.println(
                        assistantName
                                + " je dodeljen na predmet "
                                + subjectName
                                + "."
                );

                System.out.println("Broj casova: " + result.getAssignedHours());
                System.out.println("Score: " + result.getScore());
                System.out.println("Razlog: " + result.getExplanation());
                return;
            }
        }

        System.out.println(
                "Ne postoji dodela za asistenta "
                        + assistantName
                        + " i predmet "
                        + subjectName
                        + "."
        );
    }
}