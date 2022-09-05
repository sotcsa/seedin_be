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
public class UserAddress {
    @Id @GeneratedValue private Long id;
    private String userId;
    private String addressId;
    private String type;
    private boolean visible;

    /**
     * Useful constructor when id is not yet known.
     *
     */
    public UserAddress(String userId, String addressId, String type) {
        this.userId = userId;
        this.addressId = addressId;
        this.type = type;
        this.visible = visible;
    }
}
