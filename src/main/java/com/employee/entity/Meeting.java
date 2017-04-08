package com.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "meeting_department",
            joinColumns = @JoinColumn(
                    name = "meeting_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "department_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Department> departments = new HashSet<>();
}
