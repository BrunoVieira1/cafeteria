package com.example.cafeteria;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private static final Map<Long, Pedido> mapaPedidos = new ConcurrentHashMap<Long, Pedido>();
    private static final AtomicLong idPedido = new AtomicLong();

    static {
        long id1 = idPedido.incrementAndGet();
        mapaPedidos.put(id1, new Pedido(id1,"1 Expresso", new BigDecimal("9.50"), "P"));

        long id2 = idPedido.incrementAndGet();
        mapaPedidos.put(id2, new Pedido(id2,"1 cafe gelado", new BigDecimal("13.50"), "R"));
    }

    @GetMapping
    public Collection<Pedido> buscarTodos() {
        return mapaPedidos.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar pedidos", description = "Cadastra um novo pedido que não tenha cadastrado na base de memoria e dura somente o tempo de execução!")
    public Pedido criar(@RequestBody Pedido pedidoRequest) {
        Long id = idPedido.incrementAndGet();
        Pedido novoPedido = new Pedido(id, pedidoRequest.descricao(), pedidoRequest.valor(), "R");
        mapaPedidos.put(id, novoPedido);
        return novoPedido;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable long id) {
        Pedido pedido = mapaPedidos.get(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        if(!mapaPedidos.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        mapaPedidos.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedidoRequest) {
        if (!mapaPedidos.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Pedido pedidoAtualizado = new Pedido(id, pedidoRequest.descricao(), pedidoRequest.valor(), pedidoRequest.status());
        mapaPedidos.put(id, pedidoAtualizado);
        return ResponseEntity.ok(pedidoAtualizado);
    }


}
