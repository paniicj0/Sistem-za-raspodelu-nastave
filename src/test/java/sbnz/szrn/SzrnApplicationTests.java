package sbnz.szrn;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sbnz.szrn.demo.DemoDataFactory;
import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.dto.AllocationOutput;
import sbnz.szrn.model.Assistant;
import sbnz.szrn.model.Preference;
import sbnz.szrn.model.Subject;
import sbnz.szrn.service.AllocationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SzrnApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void demoAllocationRunsDroolsRules() {
        AllocationOutput output = new AllocationService().allocate(DemoDataFactory.createDemoInput());

        assertFalse(output.getAssignmentResults().isEmpty());
        assertTrue(output.getValidationMessages().isEmpty());
        assertFalse(output.getNegativeExplanations().isEmpty());
        assertTrue(output.getGeneratedDrl().contains("modify($c)"));
    }

    @Test
    void allocationInputCanBeReadFromClientJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(DemoDataFactory.createDemoInput());

        AllocationInput input = objectMapper.readValue(json, AllocationInput.class);

        assertEquals(5, input.getAssistants().size());
        assertEquals(7, input.getSubjects().size());
        assertEquals(35, input.getPreferences().size());
        assertEquals(5, input.getSpecificRequests().size());
    }

    @Test
    void lowPreferenceCanBeUsedAsFallbackWhenSubjectHasNoBetterCandidate() {
        Assistant assistant = new Assistant(1, "Fallback Asistent", 10, 0, 0);
        Subject subject = new Subject(1, "LOW", "Predmet sa niskom preferencom", 1, 1, 2);

        AllocationInput input = new AllocationInput(
                List.of(assistant),
                List.of(subject),
                List.of(new Preference(1, assistant, subject, 2)),
                List.of(),
                List.of()
        );

        AllocationOutput output = new AllocationService().allocate(input);

        assertEquals(1, output.getAssignmentResults().size());
        assertTrue(output.getCandidates().get(0).getExplanation().contains("fallback"));
        assertTrue(output.getValidationMessages().isEmpty());
    }
}
