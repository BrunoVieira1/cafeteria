package com.example.cafeteria.controllers;


import com.example.cafeteria.controllers.dto.PedidoDTO;
import com.example.cafeteria.models.Pedido;
import com.example.cafeteria.models.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gerenciador de pedidos")
public class PedidoController {

    private static final Map<Long, PedidoDTO> mapaPedidos = new ConcurrentHashMap<Long, PedidoDTO>();
    private static final AtomicLong idPedido = new AtomicLong();

    static {
        long id1 = idPedido.incrementAndGet();
        mapaPedidos.put(id1, new PedidoDTO(id1,"1 Expresso", new BigDecimal("9.50"), "P"));

        long id2 = idPedido.incrementAndGet();
        mapaPedidos.put(id2, new PedidoDTO(id2,"1 cafe gelado", new BigDecimal("13.50"), "R"));
    }


    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public Collection<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar pedidos", description = "Cadastra um novo pedido que não tenha cadastrado na base de memoria e dura somente o tempo de execução!")
    public Pedido criar(@RequestBody Pedido pedidoRequest) {
        var novoPedido = pedidoRepository.save(pedidoRequest);
        return novoPedido;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        var pedido = pedidoRepository.findById(id).orElse(null);
        if(pedido == null) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.delete(pedido);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedidoRequest) {
        var pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        pedido.setDescricao(pedidoRequest.getDescricao());
        pedido.setValor(pedidoRequest.getValor());
        pedido.setStatus(pedidoRequest.getStatus());
        pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedido);
    }


}
