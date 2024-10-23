package GerenciamentodeOSsCompressao;

public class Node {
 int freq;
 char caractere;
 Node esquerda, direita;

 public Node(char caractere, int freq) {
     this.caractere = caractere;
     this.freq = freq;
     this.esquerda = null;
     this.direita = null;
 }
 public char getCaractere() {
	    return caractere;
	  }

	  public int getFreq() {
	    return freq;
	  }
}