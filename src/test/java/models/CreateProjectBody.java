package models;

import lombok.Data;

@Data
public class CreateProjectBody {
    Boolean isPublic;
    String name, abbr;
}
