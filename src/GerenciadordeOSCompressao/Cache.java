package GerenciadordeOSCompressao;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cache {
    private TabelaHash tabelaHash;
    private final int tamanhoCache = 20;
    private Random random;

    public Cache() {
        this.tabelaHash = new TabelaHash(tamanhoCache);
        this.random = new Random();
    }

    // Função para remover um item aleatório da cache
    private void removerAleatorio() {
        List<OrdensdeServicos> todosOsItens = tabelaHash.listarTodos();
        if (!todosOsItens.isEmpty()) {
            int indexAleatorio = random.nextInt(todosOsItens.size());
            OrdensdeServicos osParaRemover = todosOsItens.get(indexAleatorio);
            tabelaHash.remover(osParaRemover.getCod());
        }
    }

    // Inserir OS na cache, aplicando a política Random se necessário
    public void inserir(OrdensdeServicos os) {
        if (tabelaHash.getNumeroElementos() >= tamanhoCache) {
            removerAleatorio(); // Remove um item aleatório se a cache estiver cheia
        }
        tabelaHash.inserir(os);
    }

    // Remover um objeto da cache
    public void remover(OrdensdeServicos os) {
        tabelaHash.remover(os.getCod());
    }

    // Buscar uma OS na cache, retornando o objeto se encontrado
    public OrdensdeServicos recuperarObjeto(int cod) {
        return tabelaHash.buscar(cod);
    }

    // Retorna a lista atual de OSs na cache
    public List<OrdensdeServicos> getCache() {
        return new LinkedList<>(tabelaHash.listarTodos());
    }
}
