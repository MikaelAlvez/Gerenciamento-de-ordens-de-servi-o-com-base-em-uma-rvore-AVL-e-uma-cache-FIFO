package GerenciamentodeOSsTreeAVL;

import java.util.List;


public class Cliente {
    private Servidor servidor;

    public Cliente(Servidor servidor) {
        this.servidor = servidor;
    }
    
    public Boolean CadastrarOS(OrdensdeServicos os) throws Exception{
    	return servidor.cadastrarOS(os);
    }
    
    public List<OrdensdeServicos> ListarOS() {
    	return servidor.listarOs();
    }
    
    public Boolean AlterarOS(OrdensdeServicos os) throws Exception{
    	return servidor.alterarOS(os);
    }
    
    public Boolean RemoverOS(int cod) throws Exception{
    	return servidor.removerOS(cod);
    }
    
    public int getRegistros() {
    	return servidor.getRegistros();
    }
    
    public OrdensdeServicos BuscarOS(int cod) throws Exception{
        return servidor.buscarOSCache(cod);
    }
}