package sbnz.szrn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BackwardChainTrace {

    private Assistant assistant;
    private Subject candidateSubject;
    private Subject previousSubject;
    private String relationChain;
    private int bonus;
    private String explanation;
}
