package GerenciamentodeOSsCompressao;

import java.util.HashMap;
import java.util.Map;

public class ContadorFrequencias {
    
    // Método que conta a frequência dos caracteres em uma string
    public static Map<Character, Integer> contarFrequencias(String mensagem) {
        Map<Character, Integer> frequencias = new HashMap<>();

        // Percorre cada caractere na mensagem
        for (int i = 0; i < mensagem.length(); i++) {
            char caractere = mensagem.charAt(i);

            // Verifica se o caractere já está no mapa
            if (frequencias.containsKey(caractere)) {
                // Incrementa a contagem se já existe
                frequencias.put(caractere, frequencias.get(caractere) + 1);
            } else {
                // Caso contrário, inicia a contagem
                frequencias.put(caractere, 1);
            }
        }
        return frequencias;
    }

    // Método principal para testar o contador de frequências
    public static void main(String[] args) {
        String mensagem = "exemplo de mensagem para contar caracteres";
        Map<Character, Integer> resultado = contarFrequencias(mensagem);

        // Imprime o resultado
        System.out.println("Frequência dos caracteres:");
        for (Map.Entry<Character, Integer> entrada : resultado.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }
}
