package com.adobe.studentapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    private String id;

    @Indexed(unique = true)
    private String uid;

    private String name;
    private String phone;
    private String fatherName;
    private String motherName;
}
