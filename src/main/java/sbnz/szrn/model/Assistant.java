package sbnz.szrn.model;


import lombok.Setter;

@Setter
public class Assistant {

    private int id;
    private String name;
    private int currentLoad;
    private int targetLoad;
    private int subjectsInSemester;

    public Assistant() {
    }

    public Assistant(String name, int currentLoad, int targetLoad, int subjectsInSemester) {
        this.name = name;
        this.currentLoad = currentLoad;
        this.targetLoad = targetLoad;
        this.subjectsInSemester = subjectsInSemester;
    }

    public String getName() {
        return name;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public int getTargetLoad() {
        return targetLoad;
    }

    public int getSubjectsInSemester() {
        return subjectsInSemester;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }
}