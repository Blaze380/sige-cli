package tech.infinitymz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.infinitymz.exceptions.CharacterLimitExceededException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private String name;
    private CurriculumPlan curriculumPlan;

    public void setName(String name) throws CharacterLimitExceededException {
        if (name.length() > 60)
            throw new CharacterLimitExceededException();
        this.name = name;
    }
}
