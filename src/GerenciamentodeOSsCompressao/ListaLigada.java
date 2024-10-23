package GerenciamentodeOSsCompressao;

import java.util.LinkedList;

class No<C> {
    C chave;
    No<C> proximo;
}

class ListaAutoAjustavel<T> {
    private No<T> primeiro = null;

    // Inserir um novo elemento no final da lista
    public void inserir(T chave) {
        No<T> novo = new No<>();
        novo.chave = chave;

        if (primeiro == null) {
            primeiro = novo;
        } else {
            No<T> atual = primeiro;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novo;
        }
    }

    // Buscar um elemento aplicando o Move-to-Front
    public T buscar(T chave) {
        No<T> atual = primeiro;
        No<T> anterior = null;

        while (atual != null) {
            if (atual.chave.equals(chave)) {
                if (anterior != null) { // Se não for o primeiro, mova para frente
                    anterior.proximo = atual.proximo;
                    atual.proximo = primeiro;
                    primeiro = atual;
                }
                return atual.chave;
            }
            anterior = atual;
            atual = atual.proximo;
        }
        return null; // Não encontrado
    }

    // Remover um elemento
    public boolean remover(T chave) {
        No<T> atual = primeiro;
        No<T> anterior = null;

        while (atual != null) {
            if (atual.chave.equals(chave)) {
                if (anterior == null) {
                    primeiro = atual.proximo; // Removendo o primeiro elemento
                } else {
                    anterior.proximo = atual.proximo;
                }
                return true;
            }
            anterior = atual;
            atual = atual.proximo;
        }
        return false; // Não encontrado
    }

    // Exibir todos os elementos da lista
    public void exibir() {
        No<T> atual = primeiro;
        while (atual != null) {
            System.out.print(atual.chave + " -> ");
            atual = atual.proximo;
        }
        System.out.println("null");
    }

    // Retorna todos os elementos da lista
    public LinkedList<T> listarTodos() {
        LinkedList<T> lista = new LinkedList<>();
        No<T> atual = primeiro;
        while (atual != null) {
            lista.add(atual.chave);
            atual = atual.proximo;
        }
        return lista;
    }

    // Verifica se a lista está vazia
    public boolean isEmpty() {
        return primeiro == null;
    }
}