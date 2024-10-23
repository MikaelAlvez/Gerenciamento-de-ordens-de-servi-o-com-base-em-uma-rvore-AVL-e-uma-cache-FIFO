package GerenciamentodeOSsCompressao;

import java.util.LinkedList;

public class TabelaHash {
    private ListaAutoAjustavel<OrdensdeServicos>[] tabela;
    private int tamanho;
    private int elementos;
    private final double fatorCargaMaximo = 0.75;

    @SuppressWarnings("unchecked")
    public TabelaHash(int tamanhoInicial) {
        this.tamanho = tamanhoInicial;
        this.elementos = 0;
        tabela = new ListaAutoAjustavel[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new ListaAutoAjustavel<>();
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
        tabela[indice].inserir(os);
        elementos++;
    }

    public OrdensdeServicos buscar(int codigo) {
        int indice = funcaoHash(codigo);
        for (OrdensdeServicos os : tabela[indice].listarTodos()) {
            if (os.getCod() == codigo) {
                return os; // Elemento encontrado
            }
        }
        return null; // Não encontrado
    }

    public void remover(int codigo) {
        int indice = funcaoHash(codigo);
        boolean removido = tabela[indice].remover(new OrdensdeServicos(codigo)); // Aqui você tentaria passar um objeto com apenas o código
        if (removido) {
            elementos--;
        }
    }

    private void redimensionar() {
        int novoTamanho = tamanho * 2;
        ListaAutoAjustavel<OrdensdeServicos>[] novaTabela = new ListaAutoAjustavel[novoTamanho];
        for (int i = 0; i < novoTamanho; i++) {
            novaTabela[i] = new ListaAutoAjustavel<>();
        }

        // Re-hash dos elementos da tabela antiga para a nova tabela
        for (ListaAutoAjustavel<OrdensdeServicos> lista : tabela) {
            LinkedList<OrdensdeServicos> ordens = lista.listarTodos();
            for (OrdensdeServicos os : ordens) {
                int novoIndice = os.getCod() % novoTamanho;
                novaTabela[novoIndice].inserir(os);
            }
        }

        // Atualiza a tabela e o tamanho
        tabela = novaTabela;
        tamanho = novoTamanho;
    }

    public LinkedList<OrdensdeServicos> listarTodos() {
        LinkedList<OrdensdeServicos> listaTodos = new LinkedList<>();
        for (ListaAutoAjustavel<OrdensdeServicos> lista : tabela) {
            listaTodos.addAll(lista.listarTodos());
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
            ListaAutoAjustavel<OrdensdeServicos> lista = tabela[i];
            LinkedList<OrdensdeServicos> ordens = lista.listarTodos();
            if (ordens.size() > 1) {
                bucketsComColisao++;
            }

            // Exibe os valores armazenados no índice
            sb.append("Index ").append(i).append("-> ");
            if (ordens.isEmpty()) {
                sb.append("vazio\n");
            } else {
                for (OrdensdeServicos os : ordens) {
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
