
package io.hashimati.usersservices.domains; 


import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.MappedProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name ="roles")
public class Role {
    @Id
    @GeneratedValue
    private Long id;


    @MappedProperty("role_name")
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}