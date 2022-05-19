package fr.acln;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Sample implements Serializable {

    private String name;
    private int age;

}
