package GerenciadordeOSCompressao;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CompressaoHuffman {
    
    // Conta as frequências dos caracteres em uma string
    public Map<Character, Integer> calcularFrequencias(String mensagem) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (char c : mensagem.toCharArray()) {
            frequencias.put(c, frequencias.getOrDefault(c, 0) + 1);
        }
        return frequencias;
    }

    // Constroi a árvore de Huffman
    public NoHuffman construirArvore(Map<Character, Integer> frequencias) {
        PriorityQueue<NoHuffman> fila = new PriorityQueue<>();

        // Cria um nó para cada caractere e insere na fila de prioridade
        for (Map.Entry<Character, Integer> entry : frequencias.entrySet()) {
            fila.add(new NoHuffman(entry.getKey(), entry.getValue()));
        }

        // Combina os nós até restar apenas um (a raiz da árvore)
        while (fila.size() > 1) {
            NoHuffman no1 = fila.poll();
            NoHuffman no2 = fila.poll();
            NoHuffman novoNo = new NoHuffman('\0', no1.frequencia + no2.frequencia);  // '\0' é um caractere nulo
            novoNo.esquerdo = no1;
            novoNo.direito = no2;
            fila.add(novoNo);
        }

        return fila.poll();  // Raiz da árvore
    }

    // Gera os códigos de Huffman para cada caractere
    public void gerarCodigos(NoHuffman raiz, String codigoAtual, Map<Character, String> codigos) {
        if (raiz == null) return;

        // Se for uma folha, armazena o código
        if (raiz.esquerdo == null && raiz.direito == null) {
            codigos.put(raiz.caractere, codigoAtual);
        }

        // Recursivamente gera os códigos para subárvores esquerda e direita
        gerarCodigos(raiz.esquerdo, codigoAtual + "0", codigos);
        gerarCodigos(raiz.direito, codigoAtual + "1", codigos);
    }

    // Método para compressão da mensagem
    public String comprimir(String mensagem, Map<Character, String> codigos) {
        StringBuilder mensagemComprimida = new StringBuilder();
        for (char c : mensagem.toCharArray()) {
            mensagemComprimida.append(codigos.get(c));
        }
        return mensagemComprimida.toString();
    }
}
