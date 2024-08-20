package com.prueba.usuario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;
    private String number;
    private String citycode;
    private String countryCode;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
