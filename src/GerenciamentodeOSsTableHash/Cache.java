package GerenciamentodeOSsTableHash;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cache {
    private ArrayList<OrdensdeServicos> cache;
    private final int tamanhoCache = 20;
    private Random random;

    public Cache() {
        this.cache = new ArrayList<>();
        this.random = new Random();
    }

    // Inserir OS na cache, aplicando a política Random se necessário
    public void randomInsert(OrdensdeServicos os) {
        if (cache.size() >= tamanhoCache) {
            int indexAleatorio = random.nextInt(tamanhoCache); // Seleciona um índice aleatório
            cache.remove(indexAleatorio); // Remove o item aleatoriamente selecionado
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
