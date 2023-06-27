package models;

import lombok.Data;

@Data
public class CreateProjectResponse {
    Long id, createdDate, lastModifiedDate, projectId;
    String name, abbr, createdBy, lastModifiedBy;
    Boolean isPublic;
}
