package az.matrix.linkedinclone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EducationDegree {
    BACHELOR,
    MASTER,
    DOCTORATE,
    ASSOCIATE;

    @JsonCreator
    public static EducationDegree fromValue(String value) {
        for (EducationDegree degree : values()) {
            if (degree.name().equals(value)) {
                return degree;
            }
        }
        throw new IllegalArgumentException("Invalid degree: " + value);
    }
}

