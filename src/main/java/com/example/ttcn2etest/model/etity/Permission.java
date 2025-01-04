package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "permission")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @Column(name = "permission_id")
    private String permissionId;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;

    @Column(name = "create_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
