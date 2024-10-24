package CacheEviction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Cache {
    private OrdemServico[] cache;
    private final int tamanhoCache = 30;
    private int tamanho;
    private Random random;
    
    public Cache() {
        this.cache = new OrdemServico[tamanhoCache];
        this.tamanho = 0;
        this.random = new Random();
    }

    public void adicionar(OrdemServico os) {
        if (tamanho >= tamanhoCache) {
        	removerAleatorio();
        }

        for (int i = tamanho; i > 0; i--) {
            cache[i] = cache[i - 1];
        }
        LogCache("Adicionado: " + os.imprimir());
        cache[0] = os;
        tamanho++;
    }

    // Remove um item aleatório da cache
    private void removerAleatorio() {
        if (tamanho > 0) {
            int indiceAleatorio = random.nextInt(tamanho); // Gera um índice aleatório
            OrdemServico removido = cache[indiceAleatorio];

            // Move todos os itens para preencher o espaço
            for (int i = indiceAleatorio; i < tamanho - 1; i++) {
                cache[i] = cache[i + 1];
            }
            cache[tamanho - 1] = null; // Limpa a referência
            tamanho--;
            LogCache("Removido: " + removido.imprimir());
        }
    }

    public OrdemServico buscar(int codigo) {
        for (int i = 0; i < tamanho; i++) {
            OrdemServico os = cache[i];
            if (os.getCodigo() == codigo) {

                // Move o item para o início do array
                for (int j = i; j > 0; j--) {
                    cache[j] = cache[j - 1];
                }
                cache[0] = os;
                return os;
            }
            LogCache("Consultado: " + os.imprimir());
        }
        return null;
    }
    public int contarElementos() {
        return tamanho;
    }

    public void listarCache() {
        System.out.println("Total de elementos na cache: " + contarElementos());
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Índice " + i + ": " + cache[i].imprimir() + "\n");
        }
    }

    public void remover(int codigo) {
        for (int i = 0; i < tamanho; i++) {
            if (cache[i].getCodigo() == codigo) {
                OrdemServico removido = cache[i];
                // Move todos os itens para preencher o espaço
                for (int j = i; j < tamanho - 1; j++) {
                    cache[j] = cache[j + 1];
                }
                cache[tamanho - 1] = null; // Limpa a referência
                tamanho--;
                LogCache("Removido: " + removido.imprimir());
                return;
            }
        }
    }

    private void LogCache(String msg) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String gerarStringCache() {
        StringBuilder sb = new StringBuilder();
        boolean cacheVazia = true;

        for (int i = 0; i < tamanho; i++) {
            sb.append(cache[i].imprimir());
            cacheVazia = false;
        }

        return cacheVazia ? "Cache vazia" : sb.toString();
    }
}
