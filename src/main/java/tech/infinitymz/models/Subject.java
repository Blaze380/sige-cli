package tech.infinitymz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.infinitymz.enums.SubjectPriority;
import tech.infinitymz.exceptions.CharacterLimitExceededException;
import tech.infinitymz.exceptions.NumberLimitExceededException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    private String name;
    private double numberOfCredits;
    private SubjectPriority priority;
    private int semester;
    final private Topics[] topics = new Topics[4];

    /**
     * 
     * @param name - Subject name
     * @throws CharacterLimitExceededException - If the name has more than 60
     *                                         characters
     */
    public void setName(String name) {
        if (name.length() > 60)
            throw new CharacterLimitExceededException();
        this.name = name;
    }

    public void setNumberOfCredits(double numberOfCredits) {
        if (numberOfCredits < 0.5 || numberOfCredits > 30)
            throw new NumberLimitExceededException();
        this.numberOfCredits = numberOfCredits;

    }

    public void setSemester(int semester) {
        if (semester < 1 || semester > 10)
            throw new NumberLimitExceededException("The numeric value exceeded, it must be  between of 1 and 10");
        this.semester = semester;
    }
}
