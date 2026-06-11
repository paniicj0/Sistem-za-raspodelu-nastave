package sbnz.szrn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationMessage {

    private int id;
    private String level;
    private String message;

    public ValidationMessage(int id, String level, String message) {
        this.id = id;
        this.level = level;
        this.message = message;
    }
}