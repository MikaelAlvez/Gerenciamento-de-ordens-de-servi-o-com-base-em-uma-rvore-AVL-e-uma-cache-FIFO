package GerenciamentodeOSsTableHash;

import java.util.LinkedList;

public class TabelaHash {
    private LinkedList<OrdensdeServicos>[] tabela;
    private int tamanho;

    @SuppressWarnings("unchecked")
    public TabelaHash(int tamanho) {
        this.tamanho = tamanho;
        tabela = new LinkedList[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    private int funcaoHash(int codigo) {
        return codigo % tamanho;
    }

    public void inserir(OrdensdeServicos os) {
        int indice = funcaoHash(os.getCod());
        tabela[indice].add(os);
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
        tabela[indice].removeIf(os -> os.getCod() == codigo);
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
}
