package GerenciamentodeOSsCompressao;

import java.util.List;

public class Cliente {
    private Servidor servidor;

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
}
