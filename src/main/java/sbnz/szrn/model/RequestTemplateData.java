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

    public RequestTemplateData(String assistantName, String subjectName, int priority) {
        this.assistantName = assistantName;
        this.subjectName = subjectName;
        this.priority = priority;
    }
}