package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.event.InventoryStatusChangedEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.in.command.rent.RentCreateCommand;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.RentRepo;
import com.muravev.samokatimmonolit.repo.TariffRepo;
import com.muravev.samokatimmonolit.service.RentSaver;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentSaverImpl implements RentSaver {
    private final RentRepo rentRepo;
    private final TariffRepo tariffRepo;
    private final InventoryRepo inventoryRepo;

    private final SecurityService securityService;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    @Transactional
    public RentEntity start(RentCreateCommand command) {
        ClientEntity currentClient = securityService.getCurrentClient();
        InventoryEntity inventory = inventoryRepo.findById(command.inventoryId())
                .filter(i -> i.getStatus() == InventoryStatus.PENDING)
                .orElseThrow(() -> new ApiException(StatusCode.INVENTORY_NOT_FOUND));
        OrganizationTariffEntity tariff = tariffRepo.findById(command.tariffId())
                .filter(not(OrganizationTariffEntity::isDeleted))
                .orElseThrow(() -> new ApiException(StatusCode.TARIFF_NOT_FOUND));

        inventory.setStatus(InventoryStatus.IN_WORK);
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, InventoryStatus.IN_WORK));

        RentEntity rent = new RentEntity()
                .setClient(currentClient)
                .setInventory(inventory)
                .setTariff(tariff)
                .setStartTime(ZonedDateTime.now());

        return rentRepo.save(rent);
    }

    @Override
    @Transactional
    public RentEntity end(long id) {
        ClientEntity currentClient = securityService.getCurrentClient();
        RentEntity rent = rentRepo.findByIdAndClient(id, currentClient)
                .orElseThrow(() -> new ApiException(StatusCode.RENT_NOT_FOUND));
        rent.setEndTime(ZonedDateTime.now());
        InventoryEntity inventory = rent.getInventory();
        inventory.setStatus(InventoryStatus.PENDING);
        eventPublisher.publishEvent(InventoryStatusChangedEvent.of(inventory, InventoryStatus.PENDING));
        return rent;
    }
}