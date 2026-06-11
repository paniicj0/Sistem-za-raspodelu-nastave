package sbnz.szrn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class Subject {

    private int id;
    private String code;
    private String name;
    private int semester;
    private int groups;
    private int hoursPerGroup;
    private int totalHours;

    public Subject(int id, String code, String name, int semester, int groups, int hoursPerGroup) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.groups = groups;
        this.hoursPerGroup = hoursPerGroup;
        this.totalHours = groups * hoursPerGroup;
    }

    public boolean isLargeSubject() {
        return totalHours >= 10;
    }

    public int getFirstHalfHours() {
        int firstHalfGroups = groups / 2;
        return firstHalfGroups * hoursPerGroup;
    }

    public int getSecondHalfHours() {
        return totalHours - getFirstHalfHours();
    }
}