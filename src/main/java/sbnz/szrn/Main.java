package sbnz.szrn;

import org.drools.template.ObjectDataCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.AssignmentResult;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.PreviousAssignment;
import sbnz.szrn.model.RequestTemplateData;
import sbnz.szrn.model.SpecificRequest;
import sbnz.szrn.model.Subject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<RequestTemplateData> templateData =
                new ArrayList<>();

        templateData.add(
                new RequestTemplateData(
                        "Ana Aničić",
                        "Baze podataka",
                        5
                )
        );

        templateData.add(
                new RequestTemplateData(
                        "Marko Marković",
                        "Programiranje 1",
                        4
                )
        );


        InputStream templateStream =
                Main.class.getResourceAsStream(
                        "/templates/request-template.drt"
                );

        ObjectDataCompiler compiler =
                new ObjectDataCompiler();

        String generatedDrl =
                compiler.compile(
                        templateData,
                        templateStream
                );

        System.out.println();
        System.out.println("GENERISANA TEMPLATE PRAVILA:");
        System.out.println(generatedDrl);


        KieHelper kieHelper =
                new KieHelper();

        kieHelper.addResource(
                ResourceFactory.newClassPathResource(
                        "rules/assignment-rules.drl"
                ),
                ResourceType.DRL
        );

        // TEMPLATE RULES
        kieHelper.addContent(
                generatedDrl,
                ResourceType.DRL
        );

        KieSession kieSession =
                kieHelper.build().newKieSession();

        Assistant marko =
                new Assistant(
                        "Marko Marković",
                        10,
                        20,
                        1
                );

        Assistant ana =
                new Assistant(
                        "Ana Aničić",
                        18,
                        22,
                        2
                );

        Assistant petar =
                new Assistant(
                        "Petar Petrović",
                        28,
                        30,
                        3
                );

        Subject programiranje =
                new Subject(
                        "Programiranje 1",
                        1,
                        4
                );

        Subject baze =
                new Subject(
                        "Baze podataka",
                        1,
                        4
                );

        Subject algoritmi =
                new Subject(
                        "Algoritmi",
                        1,
                        4
                );


        kieSession.insert(marko);
        kieSession.insert(ana);
        kieSession.insert(petar);

        kieSession.insert(programiranje);
        kieSession.insert(baze);
        kieSession.insert(algoritmi);


        kieSession.insert(
                new Preference(
                        marko,
                        programiranje,
                        5
                )
        );

        kieSession.insert(
                new Preference(
                        petar,
                        algoritmi,
                        5
                )
        );

        kieSession.insert(
                new Preference(
                        ana,
                        baze,
                        4
                )
        );


        kieSession.insert(
                new PreviousAssignment(
                        marko,
                        programiranje
                )
        );

        kieSession.insert(
                new SpecificRequest(
                        ana,
                        baze,
                        5
                )
        );

        int firedRules =
                kieSession.fireAllRules();

        System.out.println();

        System.out.println(
                "BROJ AKTIVIRANIH PRAVILA: "
                        + firedRules
        );

        Collection<?> facts =
                kieSession.getObjects();

        for (Object fact : facts) {

            if (fact instanceof AssignmentResult result) {

                System.out.println();

                System.out.println(
                        "ASISTENT: "
                                + result.getAssistant().getName()
                );

                System.out.println(
                        "PREDMET: "
                                + result.getSubject().getName()
                );

                System.out.println(
                        "BROJ CASOVA: "
                                + result.getAssignedHours()
                );

                System.out.println(
                        "OBJASNJENJE: "
                                + result.getExplanation()
                );

            }
        }

        explainAssignment(
                kieSession,
                "Marko Marković",
                "Programiranje 1"
        );

        kieSession.dispose();
    }

    public static void explainAssignment(
            KieSession kieSession,
            String assistantName,
            String subjectName
    ) {

        Collection<?> facts =
                kieSession.getObjects();

        for (Object fact : facts) {

            if (fact instanceof AssignmentResult result) {

                if (
                        result.getAssistant()
                                .getName()
                                .equals(assistantName)

                                &&

                                result.getSubject()
                                        .getName()
                                        .equals(subjectName)
                ) {

                    System.out.println();

                    System.out.println(
                            "BACKWARD CHAINING OBJASNJENJE"
                    );

                    System.out.println(
                            "Asistent: "
                                    + assistantName
                    );

                    System.out.println(
                            "Predmet: "
                                    + subjectName
                    );

                    System.out.println(
                            "Razlog dodele:"
                    );

                    System.out.println(
                            result.getExplanation()
                    );
                }
            }
        }
    }
}