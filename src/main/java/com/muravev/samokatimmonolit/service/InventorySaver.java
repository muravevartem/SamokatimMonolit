package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmessage.GeoPointReceivedMessage;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.model.in.command.inventory.*;

public interface InventorySaver {
    InventoryEntity create(InventoryCreateCommand createCommand);
    InventoryEntity changeField(long id, InventoryChangeStatusCommand command);
    InventoryEntity changeField(long id, InventoryChangeAliasCommand command);
    InventoryEntity changeField(long id, InventoryChangeModelCommand command);
    InventoryEntity changeField(long id, InventoryChangeClassCommand command);
    InventoryEntity changeField(long id, InventoryAddTariffCommand command);
    InventoryEntity changeField(long id, InventoryDeleteTariffCommand command);
    InventoryEntity changeField(long id, InventoryChangeOfficeCommand command);
    InventoryEntity changeField(long id, InventoryResetOfficeCommand command);
    void delete(long id);
    void savePoint(GeoPointReceivedMessage message);
}
