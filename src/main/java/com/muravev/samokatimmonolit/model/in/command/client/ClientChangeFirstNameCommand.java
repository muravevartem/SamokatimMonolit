package com.muravev.samokatimmonolit.model.in.command.client;

import jakarta.validation.constraints.NotBlank;

public record ClientChangeFirstNameCommand(
        @NotBlank String value
) {
}
