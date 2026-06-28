package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestTemplateData {

    private String assistantName;
    private String subjectName;
    private int priority;
    private int requestedHours;
    private boolean hasRequestedHours;

    public RequestTemplateData(String assistantName, String subjectName, int priority, Integer requestedHours) {
        this.assistantName = assistantName;
        this.subjectName = subjectName;
        this.priority = priority;
        this.requestedHours = requestedHours == null ? 0 : requestedHours;
        this.hasRequestedHours = requestedHours != null;
    }
}
