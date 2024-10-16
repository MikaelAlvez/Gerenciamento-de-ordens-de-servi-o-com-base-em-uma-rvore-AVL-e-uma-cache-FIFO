package GerenciadordeOSCompressao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cliente {
    private Servidor servidor;
    private CompressaoHuffman compressaoHuffman = new CompressaoHuffman();

    public Cliente(Servidor servidor) {
        this.servidor = servidor;
    }
    
    public boolean cadastrarOS(OrdensdeServicos os) throws Exception {
        return servidor.cadastrarOS(os);
    }
    
    public List<OrdensdeServicos> listarOS() {
        return servidor.listarOs();
    }
    
    public boolean alterarOS(OrdensdeServicos os) throws Exception {
        return servidor.alterarOS(os);
    }
    
    public boolean removerOS(int cod) throws Exception {
        return servidor.removerOS(cod);
    }
    
    public int getRegistros() {
        return servidor.getRegistros();
    }
    
    public OrdensdeServicos buscarOS(int cod) throws Exception {
        return servidor.buscarOSCache(cod);
    }
    

    public MensagemComprimida enviarMensagem(Mensagem mensagem) {
        String conteudoMensagem = mensagem.toString();
        Map<Character, Integer> frequencias = compressaoHuffman.calcularFrequencias(conteudoMensagem);
        NoHuffman arvore = compressaoHuffman.construirArvore(frequencias);
        Map<Character, String> codigos = new HashMap<>();
        compressaoHuffman.gerarCodigos(arvore, "", codigos);

        // Comprimir a mensagem
        String mensagemComprimida = compressaoHuffman.comprimir(conteudoMensagem, codigos);
        
        // Enviar mensagem comprimida e frequências
        return new MensagemComprimida(mensagemComprimida, frequencias);
    }
}
