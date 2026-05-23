package sbnz.szrn.model;

public class Subject {

    private int id;
    private String name;
    private int semester;
    private int totalHours;

    public Subject() {
    }

    public Subject(String name, int semester, int totalHours) {
        this.name = name;
        this.semester = semester;
        this.totalHours = totalHours;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public int getTotalHours() {
        return totalHours;
    }
}