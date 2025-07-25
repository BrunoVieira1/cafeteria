package com.example.cafeteria;

import java.math.BigDecimal;

public record Pedido(
        Long id,
        String descricao,
        BigDecimal valor,
        String status
) {

}
