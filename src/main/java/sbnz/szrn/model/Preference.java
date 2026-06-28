package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Preference {

    private int id;
    private Assistant assistant;
    private Subject subject;
    private int grade;

    public Preference(int id, Assistant assistant, Subject subject, int grade) {
        this.id = id;
        this.assistant = assistant;
        this.subject = subject;
        this.grade = grade;
    }


}