
/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */
package io.hashimati.usersservices.domains; 

import io.micronaut.data.annotation.GeneratedValue;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import lombok.NoArgsConstructor;
// import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @EqualsAndHashCode
// @ToString
@Entity
@Table(name="users")
public class User
{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "username", unique = true, nullable = false, length = 26, updatable = false)
    private String username;


    @NotNull
    @Column(name = "password", nullable = false)
    private String password;




    private String roles ;

    public User(){}
    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
//        this.role = role;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId()
    {
        return this.id;
    }
}
