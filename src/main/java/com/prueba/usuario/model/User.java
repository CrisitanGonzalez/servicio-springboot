package com.prueba.usuario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prueba.usuario.validators.RegistroValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@RegistroValidator
public class User {

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;
    private String name;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;
    @JsonIgnore
    private Date created;
    @JsonIgnore
    private boolean isactive;
    @JsonIgnore
    private Date modified;
    @JsonIgnore
    private Date last_login;
    @JsonIgnore
    private String token;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    @JsonProperty
    public UUID getId() {
        return id;
    }

    @JsonProperty
    public Date getCreated() {
        return created;
    }

    @JsonProperty
    public boolean isIsactive() {
        return isactive;
    }

    @JsonProperty
    public Date getModified() {
        return modified;
    }

    @JsonProperty
    public Date getLast_login() {
        return last_login;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }

}
