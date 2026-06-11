package sbnz.szrn.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Assistant {

    private int id;
    private String name;
    private int targetLoad;
    private int winterLoad;
    private int summerLoad;
    private int maxLoad;
    private boolean placeholder;


    public Assistant(int id, String name, int targetLoad, int winterLoad, int summerLoad) {
        this.id = id;
        this.name = name;
        this.targetLoad = targetLoad;
        this.winterLoad = winterLoad;
        this.summerLoad = summerLoad;
        this.maxLoad = targetLoad + 10;
        this.placeholder = false;
    }

    public int getTotalLoad() {
        return winterLoad + summerLoad;
    }

    public void addLoad(int semester, int hours) {
        if (semester == 1) {
            winterLoad += hours;
        } else {
            summerLoad += hours;
        }
    }
}