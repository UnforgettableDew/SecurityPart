package com.unforgettable.securitypart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unforgettable.securitypart.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonProperty("max_score")
    private Float maxScore;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LaboratoryWorkDTO> laboratoryWorks;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.maxScore = task.getMaxScore();
    }

//    public TaskDTO(Long id, Float maxScore) {
//        this.id = id;
//        this.maxScore = maxScore;
//    }

    public TaskDTO(Long id, String title, Float maxScore) {
        this.id = id;
        this.title = title;
        this.maxScore = maxScore;
    }
}
