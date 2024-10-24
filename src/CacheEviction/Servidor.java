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
    //private String oc;

    public Servidor() {
        tabelaHash = new TabelaHash();
        cache = new Cache();
        this.hits = 0;
        this.misses = 0; 
        //this.oc = log();
    }

    public void processarMensagem(Mensagem mensagem) {
        // Descomprimir os dados da mensagem
        int cod = mensagem.getCod();
        String nomeDescomprimido = mensagem.descomprimirNome();
        String descricaoDescomprimida = mensagem.descomprimirDescricao();
        String horaDescomprimida = mensagem.descomprimirHora();
        String operacao = mensagem.descomprimirOperacao();

        switch (operacao) {
            case "Cadastrar":
                OrdensdeServicos novaOS = new OrdensdeServicos(cod, nomeDescomprimido, descricaoDescomprimida, horaDescomprimida);
                cadastrarOrdemServico(novaOS);
                break;

            case "Alterar":
                OrdensdeServicos osEditada = new OrdensdeServicos(cod, nomeDescomprimido, descricaoDescomprimida, horaDescomprimida);
                atualizarOrdemServico(osEditada);
                break;

            case "Remover":
                OrdensdeServicos osRemover = buscarOrdemServico(cod, true);
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
                OrdensdeServicos osBuscada = buscarOrdemServico(cod, false);
                if (osBuscada != null) {
                    System.out.println(osBuscada);
                } else {
                    System.out.println("Ordem de Serviço não encontrada.\n");
                }
                break;
            /*case "Ocorrencias":
                ocorrencias(cod);*/
            default:
                System.out.println("Operação não reconhecida: " + operacao + "\n");
        }
    }


    public void cadastrarOrdemServico(OrdensdeServicos os) {
        escreverLog("Insercao de Ordem de Servico: " + os.getCodigo());
        tabelaHash.inserir(os.getCodigo(), os);
        OrdensdeServicos osBuscada = tabelaHash.buscar(os.getCodigo());

        cache.adicionar(osBuscada);
        escreverLogCache("Adicionado à cache: " + osBuscada.getCodigo());
        escreverLogCache("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
    }

    public void removerOrdemServico(OrdensdeServicos removeOS) {
        escreverLog("Remoção de Ordem de Serviço: " + removeOS.getCodigo());

        cache.remover(removeOS.getCodigo());
        tabelaHash.remover(removeOS.getCodigo());
        
        escreverLogCache("Removido da cache: " + removeOS.getCodigo());
        escreverLogCache("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());

    }

    public void atualizarOrdemServico(OrdensdeServicos novaOS) {

        OrdensdeServicos osBuscada = buscarNaCache(novaOS.getCodigo());

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

    public OrdensdeServicos buscarNaCache(int codigo) {
        OrdensdeServicos os = cache.buscar(codigo);
        return os;
    }

    public OrdensdeServicos buscarOrdemServico(int codigo, boolean isRemove) {
        OrdensdeServicos buscado = buscarNaCache(codigo);
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
    
    /*public void ocorrencias(int operacao) {
        oc = log(); // Lê o log de novo
        switch (operacao) {
            case 1:
                List<Integer> insercaoOcorrencias = buscarOcorrencias("Insercao de Ordem de Servico");
                System.out.println("Ocorrências de Inserção: " + insercaoOcorrencias.size());
                break;
            case 2:
                List<Integer> remocaoOcorrencias = buscarOcorrencias("Remoção de Ordem de Serviço");
                System.out.println("Ocorrências de Remoção: " + remocaoOcorrencias.size());
                break;
            case 3:
                List<Integer> alteracaoOcorrencias = buscarOcorrencias("Alteracao na Ordem de Servido");
                System.out.println("Ocorrências de Alteração: " + alteracaoOcorrencias.size());
                break;
            case 4:
                List<Integer> buscaOcorrencias = buscarOcorrencias("Item buscado");
                System.out.println("Ocorrências de Busca: " + buscaOcorrencias.size());
                break;
            default:
                break;
        }
    }
    
    public List<Integer> buscarOcorrencias(String operacao) {
        List<Integer> ocorrencias = new ArrayList<>();
        int[] lps = construir(operacao);
        int i = 0; // logs
        int j = 0; // operacao

        while (i < oc.length()) {
            // Se os caracteres são iguais, incrementa ambos
            if (operacao.charAt(j) == oc.charAt(i)) {
                i++;
                j++;
            }

            if (j == operacao.length()) {
                ocorrencias.add(i - j); //Indice da ocorrencia
                j = lps[j - 1]; // Ajusta j
            } else if (i < oc.length() && operacao.charAt(j) != oc.charAt(i)) {
                // Descontinuidade encontrada
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        escreverLog("Buscando ocorrências da operação: " + operacao.substring(0, 8) + ". Total encontrado: " + ocorrencias.size());
        System.out.println("Índices encontrados: " + ocorrencias);
        return ocorrencias;
    }
    
    private int[] construir(String operacao) {
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
    
    private String log() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("logOperacoes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return "";
        }
        return sb.toString();
    }*/
    
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
