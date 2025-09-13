package org.example.services;


import org.example.entities.Cliente;
import org.example.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    // Injeção de Dependência: O Spring automaticamente nos dá uma instância
    // do ClienteRepository para que possamos usá-la.
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente criarCliente(String nome, String cpf, String endereco){
        // ---- LÓGICA DE NEGÓCIO ----
        // Ex: Verificar se o CPF já existe para evitar duplicatas
        if(clienteRepository.findByCpf(cpf).isPresent()){
            throw new IllegalStateException("CPF já cadastrado no sistema.");
        }

        Cliente novoCliente = new Cliente();
        novoCliente.setNome(nome);
        novoCliente.setCpf(cpf);
        novoCliente.setEndereco(endereco);

        // Pedimos ao repositório para salvar a entidade no banco de dados.
        // O método save() retorna a entidade salva (agora com um ID preenchido).
        return clienteRepository.save(novoCliente);
    }

    public Cliente buscarClientePorID(Long id){
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        // O findById retorna um Optional para tratar o caso em que o ID não existe.
        if(clienteOptional.isPresent()){
            return clienteOptional.get();
        }else{
            throw new RuntimeException("Cliente com ID "+ id +"não encontrado.");
        }
    }

    public List<Cliente> ListarTodosClientes(){
        // Simplesmente chama o método findAll() fornecido pelo JpaRepository.
        return clienteRepository.findAll();
    }

    public Cliente atualizarEndereco(Long id, String novoEndereco){
        // 1. Busca o cliente que queremos atualizar.
        Cliente clienteExistente = buscarClientePorID(id);  // Reutilizamos nosso método

        // 2. Altera o atributo do objeto em memória.
        clienteExistente.setEndereco(novoEndereco);

        // 3. Salva a entidade modificada. O JPA é inteligente o suficiente para saber
        // que se trata de um UPDATE, pois a entidade já possui um ID.
        return clienteRepository.save(clienteExistente);
    }

    public void deletarCliente(Long id){
        // É uma boa prática verificar se o cliente existe antes de tentar deletar.
        if(!clienteRepository.existsById(id)){
          throw new RuntimeException("Cliente com ID" + id + "não encontrado para deleção. ");
        }

        clienteRepository.deleteById(id);
    }
}
