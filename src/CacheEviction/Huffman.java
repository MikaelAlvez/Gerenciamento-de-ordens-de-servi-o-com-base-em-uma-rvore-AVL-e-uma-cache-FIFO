import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class No {
    char carac;
    int freq;
    No esq;
    No dir;

    public No(char carac, int freq) {
        this.carac = carac;
        this.freq = freq;
        this.esq = null;
        this.dir = null;
    }
}

class ListaDePrioridade {
    private List<No> lista;

    public ListaDePrioridade() {
        this.lista = new ArrayList<>();
    }

    public void adicionar(No no) {
        int i = 0;
        while (i < lista.size() && lista.get(i).freq <= no.freq) {
            i++;
        }
        lista.add(i, no);
    }

    public No removerMenor() {
        if (!lista.isEmpty()) {
            return lista.remove(0);
        }
        return null;
    }

    public int tamanho() {
        return lista.size();
    }
}

public class Huffman {
    private No raiz;
    private Map<Character, String> huffman = new HashMap<>(); 

    private Map<Character, Integer> contarFrequencias(String texto) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char letra : texto.toCharArray()) {
            freq.put(letra, freq.getOrDefault(letra, 0) + 1);
        }
        return freq;
    }

    private No ArvoreHuffman(Map<Character, Integer> frequencias) {
        ListaDePrioridade listaPrioridade = new ListaDePrioridade();

        for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
        	No novoNo = new No(entrada.getKey(), entrada.getValue());
            listaPrioridade.adicionar(novoNo);
        }

        while (listaPrioridade.tamanho() > 1) {

        	No menor1 = listaPrioridade.removerMenor();
        	No menor2 = listaPrioridade.removerMenor();

        	No novoNo = new No('\0', menor1.freq + menor2.freq);
            novoNo.esq = menor1;
            novoNo.dir = menor2;

            listaPrioridade.adicionar(novoNo);
        }

        return listaPrioridade.removerMenor();
    }

    private void gerarCodigos(No no, String codigo) {
        if (no == null) {
            return;
        }
        if (no.esq == null && no.dir == null) {
        	huffman.put(no.carac, codigo);
        } else {
            gerarCodigos(no.esq, codigo + '0');
            gerarCodigos(no.dir, codigo + '1');
        }
    }

    public String comprimir(String texto) {
        StringBuilder textoComprimido = new StringBuilder();
        for (char letra : texto.toCharArray()) {
            String codigo = huffman.get(letra);
            if (codigo != null) {
                textoComprimido.append(codigo);
            }
        }
        return textoComprimido.toString();
    }

    public String descomprimir(String textoComprimido) {
        StringBuilder textoOriginal = new StringBuilder();
        No no = raiz;

        for (char c : textoComprimido.toCharArray()) {
            no = (c == '0') ? no.esq : no.dir; 
            if (no.esq == null && no.dir == null) {
                textoOriginal.append(no.carac);
                no = raiz;
            }
        }
        return textoOriginal.toString();
    }

    public void inicializar(String[] textos) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (String texto : textos) {
            Map<Character, Integer> freqTexto = contarFrequencias(texto);
            for (Map.Entry<Character, Integer> entrada : freqTexto.entrySet()) {
                frequencias.put(entrada.getKey(), frequencias.getOrDefault(entrada.getKey(), 0) + entrada.getValue());
            }
        }
        raiz = ArvoreHuffman(frequencias);
        gerarCodigos(raiz, "");
    }
}
