package sbnz.szrn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRelation {

    private int id;
    private Subject source;
    private Subject target;
    private String relationType;
}
