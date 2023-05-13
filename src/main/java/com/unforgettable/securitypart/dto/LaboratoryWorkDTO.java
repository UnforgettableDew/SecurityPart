package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.entity.LaboratoryWork;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LaboratoryWorkDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonProperty(value = "github_reference")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String githubReference;
    private Float score;
    @JsonProperty("is_assess")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAssess;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comment;
    private TaskDTO task;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String course;

    @JsonProperty("students_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer studentsCount;

    public LaboratoryWorkDTO(LaboratoryWork laboratoryWork) {
        this.id = laboratoryWork.getId();
        this.title = laboratoryWork.getTitle();
        this.githubReference = laboratoryWork.getGithubReference();
        this.task = new TaskDTO(laboratoryWork.getTask());
        this.score = laboratoryWork.getScore();
        this.isAssess = laboratoryWork.getIsPassed();
        this.course = laboratoryWork.getTask().getCourse().getTitle();
    }

    public LaboratoryWorkDTO(Long id, Float score, Boolean isAssess) {
        this.id = id;
        this.score = score;
        this.isAssess = isAssess;
    }

    public LaboratoryWorkDTO(Long id, Float score, Boolean isAssess, String githubReference) {
        this.id = id;
        this.score = score;
        this.isAssess = isAssess;
        this.githubReference = githubReference;
    }

    public LaboratoryWorkDTO(Long id,
                             String title,
                             String githubReference,
                             Float score,
                             Boolean isAssess,
                             String comment) {
        this.id = id;
        this.title = title;
        this.githubReference = githubReference;
        this.score = score;
        this.isAssess = isAssess;
        this.comment = comment;
    }

    public LaboratoryWorkDTO(Float score) {
        this.score = score;
    }
}
