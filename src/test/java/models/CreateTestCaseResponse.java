package models;

import lombok.Data;

@Data
public class CreateTestCaseResponse {
    Long id, createdDate;
    String name, statusName, statusColor;
    Boolean automated, external;
}


