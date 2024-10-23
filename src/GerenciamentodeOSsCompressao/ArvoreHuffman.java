package GerenciamentodeOSsCompressao;

public class ArvoreHuffman {
	Node raiz;

    void construirArvore(int n, char[] arrayCaracteres, int[] arrayFrequencias) {
        FilaPrioridade filaPrioridade = new FilaPrioridade(n);

        for (int i = 0; i < n; i++) {
        	Node no = new Node(arrayCaracteres[i], arrayFrequencias[i]);
            filaPrioridade.insere(no);
        }

        while (!filaPrioridade.vazia() && filaPrioridade.getTamanho() > 1) {
        	Node x = filaPrioridade.remove();
        	Node y = filaPrioridade.remove();

        	Node z = new Node('-', x.freq + y.freq);
            z.esquerda = x;
            z.direita = y;

            filaPrioridade.insere(z);
        }
        raiz = filaPrioridade.remove();
    }

    void imprimirCodigo(Node no, String s) {
        if (no.esquerda == null && no.direita == null) {
            System.out.println(no.caractere + ": " + s);
            return;
        }
        imprimirCodigo(no.esquerda, s + "0");
        imprimirCodigo(no.direita, s + "1");
    }

    public void gerarCodigo() {
        imprimirCodigo(raiz, "");
    }
}
