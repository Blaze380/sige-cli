package tech.infinitymz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    private String prefix;
    private String description;
    private Argument args[];

}

@Data
class Argument {
    private String name;
    private String description;
}
