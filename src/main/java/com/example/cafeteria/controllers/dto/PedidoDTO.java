package com.example.cafeteria.controllers.dto;

import java.math.BigDecimal;

public record PedidoDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        String status
) {

}
