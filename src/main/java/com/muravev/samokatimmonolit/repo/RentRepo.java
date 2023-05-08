package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface RentRepo extends JpaRepository<RentEntity, Long> {
    Page<RentEntity> findAllByClientAndEndTimeIsNotNull(@NonNull ClientEntity client, Pageable pageable);
    @Query("""
            SELECT DISTINCT rent FROM RentEntity rent
            WHERE rent.inventory.lastMonitoringRecord.lng BETWEEN :lngSW AND :lngNE
                AND
                rent.inventory.lastMonitoringRecord.lat BETWEEN :latSW AND :latNE
                AND
                rent.inventory.activeRent.client = :client
                AND
                rent.endTime IS NULL
            ORDER BY rent.inventory.id DESC
            """)
    List<RentEntity> findAllByView(@Param("latNE") double latNE,
                                   @Param("lngNE") double lngNE,
                                   @Param("latSW") double latSW,
                                   @Param("lngSW") double lngSW,
                                   @Param("client") ClientEntity client);

    Optional<RentEntity> findByIdAndClient(Long id, ClientEntity client);

    @Query("""
            SELECT rent FROM RentEntity rent
            WHERE rent.client = :client AND rent.endTime IS NOT NULL
            """)
    Page<RentEntity> findAllCompletedRents(ClientEntity client, Pageable pageable);
}