package GerenciadordeOSCompressao;

class NoHuffman implements Comparable<NoHuffman> {
    char caractere;
    int frequencia;
    NoHuffman esquerdo, direito;

    public NoHuffman(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esquerdo = null;
        this.direito = null;
    }

    @Override
    public int compareTo(NoHuffman outro) {
        return Integer.compare(this.frequencia, outro.frequencia);
    }
}
