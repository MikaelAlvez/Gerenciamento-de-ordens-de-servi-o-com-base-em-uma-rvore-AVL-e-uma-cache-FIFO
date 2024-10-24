package CacheEviction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private TabelaHash tabelaHash;
    private Cache cache;
    private Huffman huffman;
    private int hits;
    private int misses;


    public Servidor() {
        tabelaHash = new TabelaHash();
        cache = new Cache();
        this.hits = 0;
        this.misses = 0; 
    }

    public void processarMensagem(Mensagem mensagem) {
        // Descomprimir os dados da mensagem
        int cod = mensagem.getCod();
        String nomeDescomprimido = mensagem.descomprimirNome(); // Descomprimir nome
        String descricaoDescomprimida = mensagem.descomprimirDescricao(); // Descomprimir descrição
        String horaDescomprimida = mensagem.descomprimirHora(); // Descomprimir hora
        String operacao = mensagem.descomprimirOperacao(); // Descomprimir operação

        // Processar a operação com base no tipo de mensagem
        switch (operacao) {
            case "Cadastrar":
                // Criar nova OS com os dados descomprimidos
                OrdemServico novaOS = new OrdemServico(cod, nomeDescomprimido, descricaoDescomprimida, horaDescomprimida);
                cadastrarOrdemServico(novaOS); // Cadastrar a ordem de serviço
                break;

            case "Alterar":
                // Atualizar OS com os dados descomprimidos
                OrdemServico osEditada = new OrdemServico(cod, nomeDescomprimido, descricaoDescomprimida, horaDescomprimida);
                atualizarOrdemServico(osEditada); // Alterar ordem de serviço
                break;

            case "Remover":
                OrdemServico osRemover = buscarOrdemServico(cod, true);
                if (osRemover != null) {
                    removerOrdemServico(osRemover);
                } else {
                    System.out.println("Ordem de Serviço não encontrada.\n");
                }
                break;

            case "Listar":
                listagem();
                break;

            case "Quantidade":
                System.out.println("Quantidade de Registros: " + quantidadeDeRegistros() + "\n");
                break;

            case "Buscar":
                OrdemServico osBuscada = buscarOrdemServico(cod, false);
                if (osBuscada != null) {
                    System.out.println(osBuscada);
                } else {
                    System.out.println("Ordem de Serviço não encontrada.\n");
                }
                break;

            default:
                System.out.println("Operação não reconhecida: " + operacao + "\n");
        }
    }


    public void cadastrarOrdemServico(OrdemServico os) {
        escreverLog("Insercao de Ordem de Servico: " + os.getCodigo());
        tabelaHash.inserir(os.getCodigo(), os);
        OrdemServico osBuscada = tabelaHash.buscar(os.getCodigo());

        cache.adicionar(osBuscada);
        escreverLogCache("Adicionado à cache: " + osBuscada.getCodigo());
        escreverLogCache("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
    }

    public void removerOrdemServico(OrdemServico removeOS) {
        escreverLog("Remoção de Ordem de Serviço: " + removeOS.getCodigo());

        cache.remover(removeOS.getCodigo());
        tabelaHash.remover(removeOS.getCodigo());
        
        escreverLogCache("Removido da cache: " + removeOS.getCodigo());
        escreverLogCache("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());

    }

    public void atualizarOrdemServico(OrdemServico novaOS) {

        OrdemServico osBuscada = buscarNaCache(novaOS.getCodigo());

        if (osBuscada != null) {
            osBuscada.setNome(novaOS.getNome());
            osBuscada.setDescricao(novaOS.getDescricao());
            osBuscada.setHorario(novaOS.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            escreverLog("Cache alterada");

            escreverLog("");
            escreverLog("Alteracao na Ordem de Servido feita na cache: " + novaOS.getCodigo() + ", Time: " + now());
            escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
            return;
        }

        tabelaHash.alterarOrdemServico(novaOS);

        osBuscada = tabelaHash.buscar(novaOS.getCodigo());
        cache.remover(novaOS.getCodigo());
        cache.adicionar(osBuscada); 

        escreverLog("");
        escreverLog("Alteracao na Ordem de Servico: " + novaOS.getCodigo() + ", Time: " + now());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
    }

    public OrdemServico buscarNaCache(int codigo) {
        OrdemServico os = cache.buscar(codigo);
        return os;
    }

    public OrdemServico buscarOrdemServico(int codigo, boolean isRemove) {
        OrdemServico buscado = buscarNaCache(codigo);
        if (buscado != null) {
            hits++;
            return buscado;
        }

        buscado = tabelaHash.buscar(codigo);
        if (buscado != null) { 
            misses++;
            if (!isRemove) {
                cache.adicionar(buscado); 
                escreverLogCache("Busca: " + codigo);
            }
            return buscado;
        }

        return null;
    }

    public void listagem() {
        System.out.println();
        this.tabelaHash.imprimirTabela();
        System.out.println();
    }

    public int quantidadeDeRegistros() {
        return this.tabelaHash.getTamanho();
    }

    public int mostrarMod() {
        return this.tabelaHash.gettam();
    }

    public float mostrarFatorDeCarga() {
        return this.tabelaHash.getFatorDeCarga();
    }

    private String now() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private int[] construirLPS(String operacao) {
        int[] lps = new int[operacao.length()];
        int length = 0; // prefixo anterior
        int i = 1;

        while (i < operacao.length()) {
            if (operacao.charAt(i) == operacao.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public void escreverLog(String msg) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("logServidor.txt", true), "UTF-8"))) {
            writer.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void mostrarEstatisticas() {
    	escreverLogCache("Hits: " + hits +
        "\nMisses: " + misses);
    }
    
    private void escreverLogCache(String msg) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("logCache.txt", true), "UTF-8"))) {
            
            writer.write(msg + "\n");
            
            writer.write("----------------------------------\n");
            writer.write("Estado atual da Cache:\n");
            
            writer.write(cache.gerarStringCache()); 
            
            writer.write("\nHits: " + hits + "\nMisses: " + misses + "\n");
            writer.write("Data e Hora: " + now() + "\n");
            writer.write("----------------------------------\n");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
