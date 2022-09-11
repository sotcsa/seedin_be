package com.nearsg.jobportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Badge {
    @Id @GeneratedValue private Long id;
    private String name;
    private Long eventId;
    private Long userId;

    public Badge(String name, Long eventId, Long userId) {
        this.name = name;
        this.eventId = eventId;
        this.userId = userId;
    }
}
