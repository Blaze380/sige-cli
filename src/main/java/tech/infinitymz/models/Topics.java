package tech.infinitymz.models;

import lombok.Data;
import tech.infinitymz.exceptions.CharacterLimitExceededException;

/**
 * Topics
 */
@Data
public class Topics {

    private String topic;

    public void setTopic(String topic) throws CharacterLimitExceededException {
        if (topic.length() > 60)
            throw new CharacterLimitExceededException();
        this.topic = topic;
    }

}