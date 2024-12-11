package com.batch.manager.repository;

import com.batch.manager.entity.deviceTracker.DeviceTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceTrackerRepository extends JpaRepository<DeviceTracker, Long> {

    public Page<DeviceTracker> findByPersonDeviceTrackerIdPersonAndCreatedAtDeviceTrackerBetweenOrderByCreatedAtDeviceTracker(Long personId, LocalDateTime init, LocalDateTime end, Pageable pageable);

    @Query("""
    select dt
    from DeviceTracker dt
    where dt.personDeviceTracker.idPerson = ?1 and
    dt.createdAtDeviceTracker BETWEEN ?2 AND ?3
    order by dt.createdAtDeviceTracker asc
    """)
    public Page<DeviceTracker> filterToHistoric(Long personId, LocalDateTime init, LocalDateTime end, Pageable pageable);

    @Query("""
    select COUNT(dt)
    from DeviceTracker dt
    where dt.personDeviceTracker.idPerson = ?1 and
    dt.createdAtDeviceTracker BETWEEN ?2 AND ?3
    order by dt.createdAtDeviceTracker asc
    """)
    public Long countPoint(Long personId, LocalDateTime init, LocalDateTime end);

    @Query(value = """
    SELECT dt.*
    FROM tracker dt
    WHERE SDO_INSIDE(
            SDO_GEOMETRY(2001, 4326, SDO_POINT_TYPE(dt.longitude, dt.latitude, NULL), NULL, NULL),
            (SELECT poly FROM location WHERE id_location = :locationId)
            ) = 'TRUE'
    AND dt.created_at BETWEEN :init AND :end
    AND (:userId IS NULL OR dt.id_person = :userId)
    ORDER BY dt.created_at
    """, nativeQuery = true)
    List<DeviceTracker> findTrackersInsideLocation(
            @Param("locationId") Long locationId,
            @Param("init") LocalDateTime init,
            @Param("end") LocalDateTime end,
            @Param("userId") Long userId
    );
}
