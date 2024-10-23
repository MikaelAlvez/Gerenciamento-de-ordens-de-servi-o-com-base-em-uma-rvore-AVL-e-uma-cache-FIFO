package GerenciamentodeOSsTreeAVL;

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
    private ArvoreAVL BancodeDados;
    private Cache cache;
    private int hits;
    private int miss;

    public Servidor() {
        this.BancodeDados = new ArvoreAVL();
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
                BancodeDados.adicionar(os);
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
        if (ordem != null) {
            hits++;
            logCache();
            return ordem;
        }

        ordem = BancodeDados.buscar(cod);
        if (ordem != null) {
            cache.fifo(ordem);
            miss++;
        }

        logCache();
        return ordem;
    }

    public Boolean cadastrarOS(OrdensdeServicos os) throws Exception {
        int alturaAnt = BancodeDados.getAltura();

        BancodeDados.adicionar(os);

        if (BancodeDados.rotacionado()) {
            log("Inserção", true, alturaAnt, BancodeDados.getAltura(), os.getCod());
        } else {
            log("Inserção", false, alturaAnt, BancodeDados.getAltura(), os.getCod());
        }

        atualizarArquivo();

        return true;
    }

    public List<OrdensdeServicos> listarOs() {
        return BancodeDados.listaOS();
    }

    public Boolean alterarOS(OrdensdeServicos os) throws Exception {
        BancodeDados.alterar(os);

        OrdensdeServicos ordem = cache.recuperarObjeto(os.getCod());

        if (ordem != null) {
            cache.remover(ordem);
            cache.fifo(os);
        }

        atualizarArquivo();

        return true;
    }

    public Boolean removerOS(int cod) throws Exception {
        int alturaAnt = BancodeDados.getAltura();

        BancodeDados.remover(cod);

        OrdensdeServicos ordem = cache.recuperarObjeto(cod);

        if (ordem != null) {
            cache.remover(ordem);
        }

        if (BancodeDados.rotacionado()) {
            log("Remocao", true, alturaAnt, BancodeDados.getAltura(), cod);
        } else {
            log("Remocao", false, alturaAnt, BancodeDados.getAltura(), cod);
        }

        atualizarArquivo();

        return true;
    }

    private void atualizarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BancodeDados.txt"))) {
            List<OrdensdeServicos> listaOS = BancodeDados.listaOS();
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
        return BancodeDados.getRegistros();
    }

    private void log(String funcoes, boolean rotacao, int alturaAnt, int novaAlt, int cod) {
        String balanceamento;
        if (rotacao) {
            balanceamento = "Rotação realizada!";
        } else {
            balanceamento = "Árvore balanceada, não precisou rotacionar";
        }

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String message = String.format(
            "\n--- %s ---" +
            "\nAltura inicial: %d\n" +
            "%s do No %d\n" +
            "%s\n" +
            "Altura atual: %d\n",
            formatter.format(agora), alturaAnt, funcoes, cod, balanceamento, novaAlt);

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

            List<OrdensdeServicos> cacheAtual = cache.getCache();
            exibir.println("Estado atual da Cache: ");

            for (OrdensdeServicos os : cacheAtual) {
                exibir.println(String.format("%d", os.getCod()) + " ");
            }

            exibir.println("\nInformações de Cache:");
            exibir.println("Hits: " + hits);
            exibir.println("Misses: " + miss);
            exibir.println("-----------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArvoreAVL getBancodeDados() {
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
