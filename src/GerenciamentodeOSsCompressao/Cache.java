package GerenciamentodeOSsCompressao;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cache {
    private ListaAutoAjustavel<OrdensdeServicos> cache; // Usando Lista Autoajustável
    private final int tamanhoCache = 30;
    private Random random;

    public Cache() {
        this.cache = new ListaAutoAjustavel<>();
        this.random = new Random();
    }

    // Função para remover um item aleatório da cache
    private void removerAleatorio() {
        if (!cache.listarTodos().isEmpty()) {
            int indexAleatorio = random.nextInt(cache.listarTodos().size());
            OrdensdeServicos osParaRemover = cache.listarTodos().get(indexAleatorio);
            cache.remover(osParaRemover);
        }
    }

    // Inserir OS na cache, aplicando a política Random se necessário
    public void inserir(OrdensdeServicos os) {
        if (cache.listarTodos().size() >= tamanhoCache) {
            removerAleatorio(); // Remove um item aleatório se a cache estiver cheia
        }
        cache.inserir(os);
    }

    // Remover um objeto da cache
    public void remover(OrdensdeServicos os) {
        cache.remover(os);
    }

    // Buscar uma OS na cache, retornando o objeto se encontrado
    public OrdensdeServicos recuperarObjeto(int cod) {
        for (OrdensdeServicos os : cache.listarTodos()) {
            if (os.getCod() == cod) {
                return os;
            }
        }
        return null; // Retorna null se não encontrar o objeto
    }

    // Retorna a lista atual de OSs na cache
    public List<OrdensdeServicos> getCache() {
        return new LinkedList<>(cache.listarTodos());
    }
    
 // Método para preencher automaticamente a cache
    public void preencherCacheAutomaticamente() {
        for (int i = 1; i <= tamanhoCache; i++) {
            // Cria um código único para cada OS
            int codigo = i;
            String nome = "Ordem de Serviço " + codigo;
            String descricao = "Descrição da OS " + codigo;
            LocalDateTime horario = LocalDateTime.now(); // Defina a data/hora conforme necessário

            OrdensdeServicos os = new OrdensdeServicos(codigo, nome, descricao);
            os.setHorario(horario); // Definindo a data/hora
            inserir(os); // Insere a OS na cache
        }
    }

}
