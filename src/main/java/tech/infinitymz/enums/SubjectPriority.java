package tech.infinitymz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubjectPriority {
    OPTIONAL(10), OBLIGATORY(1);

    private int priority;
}
