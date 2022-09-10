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
public class Event {
    @Id @GeneratedValue private Long id;
    private String name;
    private String description;
    private String startDate;
    private String expiryDate;

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Badge> badges;

    /**
     * Useful constructor when id is not yet known.
     *
     * @param name
     */
    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
