package GerenciadordeOSCompressao;

import java.util.Map;

public class MensagemComprimida {
    private String mensagemComprimida;
    private Map<Character, Integer> frequencias;

    public MensagemComprimida(String mensagemComprimida, Map<Character, Integer> frequencias) {
        this.mensagemComprimida = mensagemComprimida;
        this.frequencias = frequencias;
    }

    public String getMensagemComprimida() {
        return mensagemComprimida;
    }

    public Map<Character, Integer> getFrequencias() {
        return frequencias;
    }
}
