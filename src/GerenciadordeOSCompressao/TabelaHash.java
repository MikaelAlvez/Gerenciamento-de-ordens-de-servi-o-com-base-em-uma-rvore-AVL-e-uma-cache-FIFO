package GerenciadordeOSCompressao;

import java.util.LinkedList;

public class TabelaHash {
    private LinkedList<OrdensdeServicos>[] tabela;
    private int tamanho;
    private int elementos;
    private final double fatorCargaMaximo = 0.75;

    @SuppressWarnings("unchecked")
    public TabelaHash(int tamanhoInicial) {
        this.tamanho = tamanhoInicial;
        this.elementos = 0;
        tabela = new LinkedList[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    private int funcaoHash(int codigo) {
        return codigo % tamanho;
    }

    public void inserir(OrdensdeServicos os) {
        if ((double) elementos / tamanho >= fatorCargaMaximo) {
            redimensionar(); // Redimensiona a tabela se o fator de carga for excedido
        }

        int indice = funcaoHash(os.getCod());
        tabela[indice].add(os);
        elementos++;
    }

    public OrdensdeServicos buscar(int codigo) {
        int indice = funcaoHash(codigo);
        for (OrdensdeServicos os : tabela[indice]) {
            if (os.getCod() == codigo) {
                return os;
            }
        }
        return null; // Não encontrado
    }

    public void remover(int codigo) {
        int indice = funcaoHash(codigo);
        boolean removido = tabela[indice].removeIf(os -> os.getCod() == codigo);
        if (removido) {
            elementos--;
        }
    }

    private void redimensionar() {
        int novoTamanho = tamanho * 2;
        LinkedList<OrdensdeServicos>[] novaTabela = new LinkedList[novoTamanho];
        for (int i = 0; i < novoTamanho; i++) {
            novaTabela[i] = new LinkedList<>();
        }

        // Re-hash dos elementos da tabela antiga para a nova tabela
        for (LinkedList<OrdensdeServicos> lista : tabela) {
            for (OrdensdeServicos os : lista) {
                int novoIndice = os.getCod() % novoTamanho;
                novaTabela[novoIndice].add(os);
            }
        }

        // Atualiza a tabela e o tamanho
        tabela = novaTabela;
        tamanho = novoTamanho;
    }

    public LinkedList<OrdensdeServicos> getLista(int indice) {
        return tabela[indice];
    }

    public LinkedList<OrdensdeServicos> listarTodos() {
        LinkedList<OrdensdeServicos> listaTodos = new LinkedList<>();
        for (LinkedList<OrdensdeServicos> lista : tabela) {
            listaTodos.addAll(lista);
        }
        return listaTodos;
    }
    
    public String estadoTabelaHash() {
        StringBuilder sb = new StringBuilder();
        sb.append("Número de elementos: ").append(elementos).append("\n");
        sb.append("Capacidade da tabela: ").append(tamanho).append("\n");
        sb.append("Fator de carga: ").append((double) elementos / tamanho).append("\n");

        // Checa e adiciona informações sobre colisões
        int bucketsComColisao = 0;
        for (int i = 0; i < tabela.length; i++) {
            LinkedList<OrdensdeServicos> lista = tabela[i];
            if (lista.size() > 1) {
                bucketsComColisao++;
            }

            // Exibe os valores armazenados no índice
            sb.append("Index ").append(i).append("-> ");
            if (lista.isEmpty()) {
                sb.append("vazio\n");
            } else {
                for (OrdensdeServicos os : lista) {
                    sb.append(os.getCod()).append(" ");
                }
                sb.append("\n");
            }
        }

        sb.append("Buckets com colisão: ").append(bucketsComColisao).append("\n");

        return sb.toString();
    }


    public int getTamanhoAtual() {
        return tamanho;
    }

    public int getNumeroElementos() {
        return elementos;
    }
}
