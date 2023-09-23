package com.cybercenter.core.constant;

import com.cybercenter.core.exception.EXPInvalidEducation;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.stream.Stream;

@Getter
public enum Education {
    SCHOOL(1, "زیر دیپلم"),
    DIPLOMA(2, "دیپلم"),
    BACHELOR(3, "لیسانس"),
    MASTER(4, "فوق لیسانس"),
    PH(5, "دکتری");

    private int id;
    private String title;

    Education(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Education of(int id){
        if (ObjectUtils.isEmpty(id))
            throw new EXPInvalidEducation();

        return Stream.of(values())
                .filter(education -> education.id == id)
                .findFirst()
                .orElseThrow(EXPInvalidEducation::new);
    }

}
