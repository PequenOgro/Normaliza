package org.example.controllers;


import org.example.entities.Cliente;
import org.example.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Anotação principal que transforma a classe em um Controller REST
@RequestMapping("/api/clientes") // Define a URL base para todos os métodos nesta classe
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // --- CREATE ---
    // Mapeia requisições HTTP POST para /api/clientes
    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente){
        // @RequestBody converte o JSON do corpo da requisição para um objeto Cliente
        Cliente novoCliente = clienteService.criarCliente(cliente.getNome(), cliente.getCpf(), cliente.getEndereco());
        // Retorna o objeto criado e o status HTTP 201 CREATED
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    // --- READ (TODOS) ---
    // Mapeia requisições HTTP GET para /api/clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodosClientes(){
        List<Cliente> clientes = clienteService.ListarTodosClientes();
        return new ResponseEntity<>(clientes,HttpStatus.OK);
    }

    // --- READ (POR ID) ---
    // Mapeia requisições HTTP GET para /api/clientes/{id} (ex: /api/clientes/1)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id){
        // @PathVariable extrai o valor {id} da URL
        Cliente cliente = clienteService.buscarClientePorID(id);
        return new ResponseEntity<>(cliente,HttpStatus.OK);
    }

    // --- UPDATE ---
    // Mapeia requisições HTTP PUT para /api/clientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id,@RequestBody Cliente clienteDetails){
            Cliente clienteAtualizado = clienteService.atualizarEndereco(id, clienteDetails.getEndereco());
            // Aqui poderíamos ter um método de atualização mais completo
            return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
        }

    // --- DELETE ---
    // Mapeia requisições HTTP DELETE para /api/clientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        // Retorna uma resposta vazia com status 204 NO CONTENT, indicando sucesso.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
