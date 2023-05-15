package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "lw_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "max_score")
    @JsonProperty("max_score")
    private Float maxScore;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "task")
    @JsonBackReference
    @JsonIgnore
    private List<LaboratoryWork> laboratoryWorks;

    public Task(Long id) {
        this.id = id;
    }
}
