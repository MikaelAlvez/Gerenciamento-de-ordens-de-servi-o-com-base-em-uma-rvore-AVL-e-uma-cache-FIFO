package GerenciamentodeOSsCompressao;

public class FilaPrioridade {
    private Node[] nos;
    private int tamanho;

    public FilaPrioridade(int capacidade) {
        nos = new Node[capacidade];
        tamanho = 0;
    }

    public void insere(Node no) {
        if (tamanho == nos.length) {
            throw new RuntimeException("Fila de prioridade cheia");
        }
        nos[tamanho] = no;
        tamanho++;
        arrumarParaCima(tamanho - 1);
    }

    public Node remove() {
        if (tamanho == 0) {
            throw new RuntimeException("Fila de prioridade vazia");
        }
        Node raiz = nos[0];
        nos[0] = nos[tamanho - 1];
        tamanho--;
        arrumarParaBaixo(0);
        return raiz;
    }

    private void arrumarParaCima(int indice) {
        int pai = (indice - 1) / 2;
        while (indice > 0 && nos[indice].freq < nos[pai].freq) {
        	Node temp = nos[indice];
            nos[indice] = nos[pai];
            nos[pai] = temp;
            indice = pai;
            pai = (indice - 1) / 2;
        }
    }

    private void arrumarParaBaixo(int indice) {
        int menor = indice;
        int esquerda = 2 * indice + 1;
        int direita = 2 * indice + 2;

        if (esquerda < tamanho && nos[esquerda].freq < nos[menor].freq) {
            menor = esquerda;
        }
        if (direita < tamanho && nos[direita].freq < nos[menor].freq) {
            menor = direita;
        }
        if (menor != indice) {
        	Node temp = nos[indice];
            nos[indice] = nos[menor];
            nos[menor] = temp;
            arrumarParaBaixo(menor);
        }
    }
    
    public int getTamanho() {
        return tamanho;
    }

    public boolean vazia() {
        return tamanho == 0;
    }
}
