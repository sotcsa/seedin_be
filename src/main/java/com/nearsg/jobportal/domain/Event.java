package com.nearsg.jobportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @Id
    @GeneratedValue(generator = "event-sequence-generator")
    @GenericGenerator(
            name = "event-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "event_sequence")
            }
    )
    private Long id;

    private String name;
    private String description;
    private String startDate;
    private String expiryDate;

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
