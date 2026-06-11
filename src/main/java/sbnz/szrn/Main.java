package sbnz.szrn;

import org.kie.api.KieServices;
import sbnz.szrn.model.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import sbnz.szrn.model.Candidate;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.io.ResourceType;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Assistant> assistants = List.of(
                new Assistant(1, "Milan Segedinac", 20, 0, 0),
                new Assistant(2, "Ivan Nejgebauer", 20, 0, 0),
                new Assistant(3, "Zeljko Maticovic", 20, 0, 0),
                new Assistant(4, "Bojana Dragas", 20, 0, 0),
                new Assistant(5, "Dragan Vidakovic", 20, 0, 0)
        );

        List<Subject> subjects = List.of(
                new Subject(1, "ORP", "Osnove racunarstva i programiranje", 1, 4, 3),
                new Subject(2, "NUM", "Numerika", 1, 4, 2),
                new Subject(3, "ISA", "ISA", 1, 2, 2),
                new Subject(4, "WEB", "Web programiranje", 1, 3, 3),
                new Subject(5, "NWT", "Napredne web tehnologije", 1, 2, 2)
        );

        Assistant milan = assistants.get(0);
        Assistant ivan = assistants.get(1);
        Assistant zeljko = assistants.get(2);
        Assistant bojana = assistants.get(3);
        Assistant dragan = assistants.get(4);

        Subject osnove = subjects.get(0);
        Subject numerika = subjects.get(1);
        Subject isa = subjects.get(2);
        Subject web = subjects.get(3);
        Subject nwt = subjects.get(4);

        List<Preference> preferences = List.of(
                new Preference(1, milan, osnove, 5),
                new Preference(2, ivan, osnove, 3),
                new Preference(3, zeljko, osnove, 4),
                new Preference(4, bojana, osnove, 2),
                new Preference(5, dragan, osnove, 5),

                new Preference(6, milan, numerika, 2),
                new Preference(7, ivan, numerika, 5),
                new Preference(8, zeljko, numerika, 3),
                new Preference(9, bojana, numerika, 4),
                new Preference(10, dragan, numerika, 2),

                new Preference(11, milan, isa, 4),
                new Preference(12, ivan, isa, 2),
                new Preference(13, zeljko, isa, 5),
                new Preference(14, bojana, isa, 3),
                new Preference(15, dragan, isa, 4),

                new Preference(16, milan, web, 3),
                new Preference(17, ivan, web, 4),
                new Preference(18, zeljko, web, 2),
                new Preference(19, bojana, web, 5),
                new Preference(20, dragan, web, 5),

                new Preference(21, milan, nwt, 1),
                new Preference(22, ivan, nwt, 3),
                new Preference(23, zeljko, nwt, 5),
                new Preference(24, bojana, nwt, 4),
                new Preference(25, dragan, nwt, 2)
        );

        List<PreviousAssignment> previousAssignments = List.of(
                new PreviousAssignment(1, milan, osnove, 6),
                new PreviousAssignment(2, ivan, numerika, 4),
                new PreviousAssignment(3, bojana, web, 6),
                new PreviousAssignment(4, zeljko, nwt, 4)
        );

        List<SpecificRequest> specificRequests = List.of(
                new SpecificRequest(1, milan, osnove, null, 5),
                new SpecificRequest(2, bojana, web, 3, 4),
                new SpecificRequest(3, zeljko, nwt, null, 3)
        );

        printAssistants(assistants);
        printSubjects(subjects);
        printPreferences(preferences);
        printPreviousAssignments(previousAssignments);
        printSpecificRequests(specificRequests);

        List<RequestTemplateData> templateData = new ArrayList<>();

        for (SpecificRequest request : specificRequests) {
            templateData.add(
                    new RequestTemplateData(
                            request.getAssistant().getName(),
                            request.getSubject().getName(),
                            request.getPriority()
                    )
            );
        }

        InputStream templateStream = Main.class.getResourceAsStream(
                "/templates/request-template.drt"
        );

        ObjectDataCompiler compiler = new ObjectDataCompiler();

        String generatedDrl = compiler.compile(
                templateData,
                templateStream
        );

        System.out.println();
        System.out.println("GENERISANA TEMPLATE PRAVILA");
        System.out.println(generatedDrl);

        KieHelper kieHelper = new KieHelper();

        kieHelper.addResource(
                ResourceFactory.newClassPathResource("rules/assignment-rules.drl"),
                ResourceType.DRL
        );

        kieHelper.addContent(
                generatedDrl,
                ResourceType.DRL
        );

        KieSession kieSession = kieHelper.build().newKieSession();

        for (Assistant assistant : assistants) {
            kieSession.insert(assistant);
        }

        for (Subject subject : subjects) {
            kieSession.insert(subject);
        }

        for (Preference preference : preferences) {
            kieSession.insert(preference);
        }

        for (PreviousAssignment previousAssignment : previousAssignments) {
            kieSession.insert(previousAssignment);
        }

        for (SpecificRequest specificRequest : specificRequests) {
            kieSession.insert(specificRequest);
        }

        int firedRules = kieSession.fireAllRules();

        System.out.println();
        System.out.println("BROJ AKTIVIRANIH PRAVILA: " + firedRules);

        System.out.println();
        System.out.println("KANDIDATI");

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof Candidate candidate) {
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

        System.out.println();
        System.out.println("KONACNA RASPODELA");

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof AssignmentResult result) {
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

        System.out.println();
        System.out.println("OPTERECENJE ASISTENATA NAKON RASPODELE");

        for (Assistant assistant : assistants) {
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

        System.out.println();
        System.out.println("VALIDACIJA");

        boolean hasValidationMessages = false;

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof ValidationMessage message) {
                hasValidationMessages = true;

                System.out.println(
                        message.getLevel()
                                + " | "
                                + message.getMessage()
                );
            }
        }

        if (!hasValidationMessages) {
            System.out.println("Nema validacionih gresaka.");
        }

        int assignmentCount = 0;
        int totalAssignedHours = 0;
        int preferenceSum = 0;

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof AssignmentResult result) {
                assignmentCount++;
                totalAssignedHours += result.getAssignedHours();

                preferenceSum += findPreferenceGrade(
                        preferences,
                        result.getAssistant(),
                        result.getSubject()
                );
            }
        }

        double averagePreference = assignmentCount == 0
                ? 0
                : (double) preferenceSum / assignmentCount;

        System.out.println();
        System.out.println("STATISTIKA RASPODELE");

        System.out.println("Broj aktiviranih pravila: " + firedRules);
        System.out.println("Broj dodela: " + assignmentCount);
        System.out.println("Ukupno dodeljenih casova: " + totalAssignedHours);
        System.out.println("Prosecna preferenca: " + averagePreference);

        explainAssignment(
                kieSession,
                "Milan Segedinac",
                "Osnove racunarstva i programiranje"
        );

        kieSession.dispose();
    }

    private static void printAssistants(List<Assistant> assistants) {
        System.out.println();
        System.out.println("ASISTENTI");

        for (Assistant assistant : assistants) {
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

    private static void printSubjects(List<Subject> subjects) {
        System.out.println();
        System.out.println("PREDMETI");

        for (Subject subject : subjects) {
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

    private static void printPreferences(List<Preference> preferences) {
        System.out.println();
        System.out.println("PREFERENCE");

        for (Preference preference : preferences) {
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

    private static void printPreviousAssignments(List<PreviousAssignment> previousAssignments) {
        System.out.println();
        System.out.println("PRETHODNA RASPODELA");

        for (PreviousAssignment assignment : previousAssignments) {
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

    private static void printSpecificRequests(List<SpecificRequest> specificRequests) {
        System.out.println();
        System.out.println("SPECIFICNI ZAHTEVI");

        for (SpecificRequest request : specificRequests) {
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

    private static int findPreferenceGrade(
            List<Preference> preferences,
            Assistant assistant,
            Subject subject
    ) {
        for (Preference preference : preferences) {
            if (
                    preference.getAssistant() == assistant
                            &&
                            preference.getSubject() == subject
            ) {
                return preference.getGrade();
            }
        }

        return 0;
    }

    private static void explainAssignment(
            KieSession kieSession,
            String assistantName,
            String subjectName
    ) {
        System.out.println();
        System.out.println("OBJASNJENJE DODELE");

        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof AssignmentResult result) {
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