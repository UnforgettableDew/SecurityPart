package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "passed_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "github_reference")
    @JsonProperty(value = "github_reference")
    private String githubReference;

    @Column(name = "point")
    private Float point;

    @Column(name = "is_assessed")
    private Boolean isAssessed;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonManagedReference
    @JsonIgnore
    private Task task;
}
