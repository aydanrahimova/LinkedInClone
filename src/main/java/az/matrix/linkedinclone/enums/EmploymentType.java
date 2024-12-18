package az.matrix.linkedinclone.enums;

import az.matrix.linkedinclone.exception.IllegalArgumentException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EmploymentType {
    FULL_TIME,
    PART_TIME,
    FREELANCE,
    INTERNSHIP;

    @JsonCreator
    public static EmploymentType fromValue(String value) {
        for (EmploymentType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid employment type:" + value);
    }

}
