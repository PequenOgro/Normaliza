package org.example.services;


import org.example.entities.Cliente;
import org.example.entities.Itens_venda;
import org.example.entities.Produto;
import org.example.entities.Venda;
import org.example.repositories.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    // Injeção de dependência via construtor (melhor prática)
    public VendaService(VendaRepository vendaRepository){
        this.vendaRepository = vendaRepository;
    }


    /**
     * Cria uma nova venda e seus itens de forma atômica.
     * Se ocorrer qualquer erro (ex: produto sem estoque), a transação inteira
     * será revertida (rollback), e nem a venda nem os itens serão salvos.
     *
     * @param cliente O cliente que está realizando a compra.
     * @param produtos Uma lista dos produtos a serem vendidos.
     * @return A entidade Venda que foi salva no banco.
     */

    @Transactional
    public Venda criarNovaVenda(Cliente cliente, List<Produto> produtos){
        // 1. Criar a entidade Venda (o "cabeçalho")
        Venda novaVenda = new Venda();
        novaVenda.setCliente(cliente);
        novaVenda.setData(LocalDateTime.now());

        // 2. Criar cada ItemVenda e associar à Venda
        // Lógica para agrupar produtos iguais e contar as quantidades
        Map<Produto, Long> produtosAgrupados = produtos.stream()
                .collect(Collectors.groupingBy(p->p,Collectors.counting()));

        for (Map.Entry<Produto,Long> entry: produtosAgrupados.entrySet()){
            Produto produto = entry.getKey();
            Integer quantidade = entry.getValue().intValue();

            // Opcional: Adicionar lógica de verificação de estoque aqui
            // if (produto.getEstoque() < quantidade) {
            //     throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            // }

            Itens_venda item = new Itens_venda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPreco()); // Grava o preço do momento
        }
        // 3. Salvar a Venda
        // Graças ao CascadeType.ALL, ao salvar a Venda, todos os ItemVenda
        // associados a ela também serão salvos na mesma transação.
        return vendaRepository.save(novaVenda);
    }
}
