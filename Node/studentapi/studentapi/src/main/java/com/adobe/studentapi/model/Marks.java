package com.adobe.studentapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("marks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Marks {
    @Id
    private String id;

    @Indexed(unique = true)
    private String uid;

    private Semester sem1;
    private Semester sem2;
    private Semester sem3;
    private Semester sem4;
    private Semester sem5;
    private Semester sem6;
    private Semester sem7;
    private Semester sem8;
}
