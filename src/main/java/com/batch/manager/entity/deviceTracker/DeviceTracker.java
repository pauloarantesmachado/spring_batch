package com.batch.manager.entity.deviceTracker;

import com.batch.manager.entity.Person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="tracker")
public class DeviceTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tracker")
    private Long idDeviceTracker;
    @Column(name = "ito_tracker_code")
    private String idTextDeviceTracker;
    @Column(name = "created_at")
    private LocalDateTime createdAtDeviceTracker;
    @Column(name="latitude")
    private Double latitude;
    @Column(name="longitude")
    private Double longitude;

    @ManyToOne
    @JoinColumn(name="id_person")
    private Person personDeviceTracker;
}