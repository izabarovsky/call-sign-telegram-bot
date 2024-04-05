package com.izabarovsky.callsign.telegram.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "integration")
public class IntegrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp lastCallTimestamp;
    @OneToOne
    @JoinColumn(name = "callsign_id")
    private CallSignEntity callSignEntity;
}
