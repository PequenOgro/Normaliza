package org.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.entities.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// Você só precisa criar a interface!
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    // O Spring Data JPA já implementa os métodos básicos para você:
    // save(), findById(), findAll(), deleteById(), etc.

    // Você também pode criar métodos de busca personalizados.
    // Ex: Encontrar um cliente pelo CPF.
    Optional<Cliente> findByCpf(String cpf);
}
