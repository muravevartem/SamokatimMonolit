package com.muravev.samokatimmonolit.entity;

import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "org_tariff")
@Getter
@Setter
@Accessors(chain = true)
public class OrganizationTariffEntity extends AuditEntity implements Comparable<OrganizationTariffEntity>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private OrganizationEntity organization;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private BigDecimal initialPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationTariffType type;

    private ZonedDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrganizationTariffEntity that = (OrganizationTariffEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public int compareTo(OrganizationTariffEntity o) {
        return getType().compareTo(o.getType());
    }
}
