package sbnz.szrn.demo;

import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.PreviousAssignment;
import sbnz.szrn.model.SpecificRequest;
import sbnz.szrn.model.Subject;

import java.util.List;

public class DemoDataFactory {

    private DemoDataFactory() {
    }

    public static AllocationInput createDemoInput() {
        List<Assistant> assistants = List.of(
                new Assistant(1, "Milan Segedinac", 24, 0, 0),
                new Assistant(2, "Ivan Nejgebauer", 24, 0, 0),
                new Assistant(3, "Zeljko Maticovic", 24, 0, 0),
                new Assistant(4, "Bojana Dragas", 24, 0, 0),
                new Assistant(5, "Dragan Vidakovic", 24, 0, 0)
        );

        List<Subject> subjects = List.of(
                new Subject(1, "ORP", "Osnove racunarstva i programiranje", 1, 4, 3),
                new Subject(2, "NUM", "Numerika", 1, 4, 2),
                new Subject(3, "ISA", "Inteligentni sistemi", 1, 2, 2),
                new Subject(4, "WEB", "Web programiranje", 1, 3, 3),
                new Subject(5, "NWT", "Napredne web tehnologije", 1, 2, 2),
                new Subject(6, "BP", "Baze podataka", 2, 3, 2),
                new Subject(7, "ML", "Masinsko ucenje", 2, 2, 3)
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
        Subject baze = subjects.get(5);
        Subject ml = subjects.get(6);

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
                new Preference(25, dragan, nwt, 2),

                new Preference(26, milan, baze, 4),
                new Preference(27, ivan, baze, 5),
                new Preference(28, zeljko, baze, 3),
                new Preference(29, bojana, baze, 2),
                new Preference(30, dragan, baze, 4),

                new Preference(31, milan, ml, 3),
                new Preference(32, ivan, ml, 2),
                new Preference(33, zeljko, ml, 5),
                new Preference(34, bojana, ml, 4),
                new Preference(35, dragan, ml, 3)
        );

        List<PreviousAssignment> previousAssignments = List.of(
                new PreviousAssignment(1, milan, osnove, 6),
                new PreviousAssignment(2, ivan, numerika, 4),
                new PreviousAssignment(3, bojana, web, 6),
                new PreviousAssignment(4, zeljko, nwt, 4),
                new PreviousAssignment(5, ivan, baze, 6),
                new PreviousAssignment(6, zeljko, ml, 6)
        );

        List<SpecificRequest> specificRequests = List.of(
                new SpecificRequest(1, milan, osnove, null, 5),
                new SpecificRequest(2, bojana, web, 3, 4),
                new SpecificRequest(3, zeljko, nwt, null, 3),
                new SpecificRequest(4, ivan, baze, null, 4),
                new SpecificRequest(5, zeljko, ml, 3, 5)
        );

        return new AllocationInput(
                assistants,
                subjects,
                preferences,
                previousAssignments,
                specificRequests
        );
    }
}
