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
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    @GenericGenerator(
            name = "user-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "user_sequence")
            }
    )
    private Long id;

    private String firstName;
    private String lastName;
    private String nickName;
    private String aboutMe;
    private String ethAddress;
    private String nearAddress;

    /**
     * Useful constructor when id is not yet known.
     *
     * @param firstName
     * @param lastName
     */
    public User(String firstName, String lastName, String ethAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ethAddress = ethAddress;
    }

    public User(String ethAddress) {
        this.ethAddress = ethAddress;
    }

}
