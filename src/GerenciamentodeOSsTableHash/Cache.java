package GerenciamentodeOSsTableHash;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cache {
    private ArrayList<OrdensdeServicos> cache;
    private final int tamanhoCache = 20;

    public Cache() {
        this.cache = new ArrayList<>();
    }

    // Exibir os itens presentes na cache
    public void exibirCache() {
        if (cache.isEmpty()) {
            System.out.println("Memória cache vazia!");
            return;
        }
        System.out.println("Cache atual:");
        for (OrdensdeServicos os : cache) {
            System.out.println("\nCódigo: " + os.getCod() + 
                               "\nNome: " + os.getNome() + 
                               "\nDescrição: " + os.getDescricao() +
                               "\nHorário da solicitação: " + os.getHorario() + "\n");
        }
    }

    // Inserir OS na cache, aplicando a política FIFO se necessário
    public void fifo(OrdensdeServicos os) {
        if (cache.size() >= tamanhoCache) {
            cache.remove(0); // Remove o item mais antigo
        }
        cache.add(os); // Adiciona o novo item
    }

    // Remover um objeto da cache
    public void remover(OrdensdeServicos os) {
        cache.remove(os);
    }

    // Buscar uma OS na cache, retornando o objeto se encontrado
    public OrdensdeServicos recuperarObjeto(int cod) {
        for (OrdensdeServicos os : cache) {
            if (os.getCod() == cod) {
                return os;
            }
        }
        return null;
    }

    // Retorna a lista atual de OSs na cache
    public List<OrdensdeServicos> getCache() {
        return new LinkedList<>(cache);
    }
}
