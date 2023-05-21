package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "educator_id")
    @JsonBackReference
    @JsonProperty(value = "educator")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Educator educator;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Task> tasks;

    @ManyToMany(mappedBy = "courses")
    @JsonBackReference
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Student> students;

    public Course(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
