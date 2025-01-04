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
import java.util.Set;

@Entity
@Table(name = "role")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "role_id")
    private String roleId;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
    //
    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "permission_id"))
    private Collection<Permission> permissions;
}
