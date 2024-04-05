package com.izabarovsky.callsign.telegram.bot.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "callsign")
public class CallSignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private Long tgId;
    private String firstName;
    private String lastName;
    private String userName;
    @NotNull
    @Column(name = "k2_call_sign", unique = true)
    private String k2CallSign;
    @Column(unique = true)
    private String officialCallSign;
    private String qth;
    @Column(unique = true)
    private String dmrId;
    @Column(updatable = false)
    private Timestamp creationTimestamp;
    private Timestamp modificationTimestamp;

    @PrePersist
    protected void onCreate() {
        creationTimestamp = modificationTimestamp = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        modificationTimestamp = Timestamp.from(Instant.now());
    }

}
