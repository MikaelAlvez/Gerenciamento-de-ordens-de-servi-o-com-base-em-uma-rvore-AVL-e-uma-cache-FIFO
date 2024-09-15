package GerenciamentodeOSsTableHash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Servidor {
    private TabelaHash BancodeDados;
    private Cache cache;
    private int hits;
    private int miss;

    public Servidor() {
        this.BancodeDados = new TabelaHash(20);
        this.cache = new Cache();
        this.hits = 0;
        this.miss = 0;

        carregarDadosIniciais("BancodeDados.txt");
    }

    public void carregarDadosIniciais(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linha;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                int codigo = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String descricao = dados[2];
                LocalDateTime horario = LocalDateTime.parse(dados[3], formatter);

                OrdensdeServicos os = new OrdensdeServicos(codigo, nome, descricao);
                os.setHorario(horario);
                BancodeDados.inserir(os);
            }
            System.out.println("Banco de Dados carregado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar os dados iniciais: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao adicionar ordens de serviço: " + e.getMessage());
        }
    }

    public OrdensdeServicos buscarOSCache(int cod) throws Exception {
        OrdensdeServicos ordem = cache.recuperarObjeto(cod);
        int tamanhoAntes = getRegistros();

        if (ordem != null) {
            hits++;
            logCache();
            
            log("Busca na Cache", true, tamanhoAntes, getRegistros(), cod);
            
            return ordem;
        }

        ordem = BancodeDados.buscar(cod);
        if (ordem != null) {
            cache.inserir(ordem);
            miss++;
            
            log("Busca no Banco de dados", false, tamanhoAntes, getRegistros(), cod);
        }

        logCache();
        return ordem;
    }

    public Boolean cadastrarOS(OrdensdeServicos os) throws Exception {
        int tamanhoAntes = getRegistros();

        BancodeDados.inserir(os);

        if (getRegistros() > tamanhoAntes) {
            log("Inserção", true, tamanhoAntes, getRegistros(), os.getCod());
        } else {
            log("Inserção", false, tamanhoAntes, getRegistros(), os.getCod());
        }

        atualizarArquivo();

        return true;
    }

    public List<OrdensdeServicos> listarOs() {
        return BancodeDados.listarTodos();
                
    }

    public Boolean alterarOS(OrdensdeServicos os) throws Exception {
        BancodeDados.remover(os.getCod());
        BancodeDados.inserir(os);

        OrdensdeServicos ordem = cache.recuperarObjeto(os.getCod());

        if (ordem != null) {
            cache.remover(ordem);
            cache.inserir(os);
        }

        atualizarArquivo();
        
        log("Alteração", true, getRegistros(), getRegistros(), os.getCod());

        return true;
    }

    public Boolean removerOS(int cod) throws Exception {
        int tamanhoAntes = getRegistros();

        BancodeDados.remover(cod);

        OrdensdeServicos ordem = cache.recuperarObjeto(cod);

        if (ordem != null) {
            cache.remover(ordem);
        }

        if (getRegistros() < tamanhoAntes) {
            log("Remoção", true, tamanhoAntes, getRegistros(), cod);
        } else {
            log("Remoção", false, tamanhoAntes, getRegistros(), cod);
        }

        atualizarArquivo();

        return true;
    }

    private void atualizarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BancodeDados.txt"))) {
            List<OrdensdeServicos> listaOS = listarOs();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            for (OrdensdeServicos os : listaOS) {
                String linha = String.format("%d,%s,%s,%s",
                    os.getCod(),
                    os.getNome(),
                    os.getDescricao(),
                    os.getHorario().format(formatter)
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o arquivo de banco de dados: " + e.getMessage());
        }
    }

    public int getRegistros() {
        return BancodeDados.listarTodos().size();
    }

    private void log(String funcao, boolean sucesso, int tamanhoAntes, int tamanhoAtual, int cod) {
        String alteracao = sucesso ? "Operação realizada com sucesso!" : "Operação não realizada.";

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String message = String.format(
            "\n--- %s ---\n" +
            "Data e Hora: %s\n" +
            "Tamanho inicial: %d\n" +
            "%s no Registro %d\n" +
            "%s\n" +
            "Tamanho atual: %d\n",
            funcao, agora.format(formatter), tamanhoAntes, funcao, cod, alteracao, tamanhoAtual);

        try (FileWriter escreverArq = new FileWriter("log.txt", true);
             BufferedWriter escreverBuf = new BufferedWriter(escreverArq);
             PrintWriter exibir = new PrintWriter(escreverBuf)) {
            exibir.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCache() {
        try (FileWriter escreverArq = new FileWriter("logCache.txt", true);
             BufferedWriter escreverBuf = new BufferedWriter(escreverArq);
             PrintWriter exibir = new PrintWriter(escreverBuf)) {

            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            exibir.println("Estado atual da Cache:");

            List<OrdensdeServicos> cacheAtual = cache.getCache();
            for (OrdensdeServicos os : cacheAtual) {
                exibir.println(String.format("Cód: %d", os.getCod()));
            }

            exibir.println("\nInformações de Cache:");
            exibir.println("Hits: " + hits);
            exibir.println("Misses: " + miss);
            exibir.println("Data e Hora: " + agora.format(formatter));
            exibir.println("-----------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TabelaHash getBancodeDados() {
        return BancodeDados;
    }

    public Cache getCache() {
        return cache;
    }

    public int getHits() {
        return hits;
    }

    public int getMiss() {
        return miss;
    }
}
