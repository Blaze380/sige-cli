package tech.infinitymz.models;

import lombok.Data;
import tech.infinitymz.lib.collections.HashTable;

@Data
public class CurriculumPlan {
    private HashTable<String, Subject> subjects;

    /**
     * @param subjects
     */
    public CurriculumPlan() {
        this.subjects = new HashTable<>();
    }

}
