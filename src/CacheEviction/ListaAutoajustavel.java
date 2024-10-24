package CacheEviction;
import java.util.ArrayList;
import java.util.List;

public class ListaAutoajustavel {
    private List<OrdemServico> os;

    public ListaAutoajustavel() {
        this.os = new ArrayList<>();
    }

    public void adicionar(OrdemServico ordem) {
    	os.add(0, ordem);
    }

    public OrdemServico buscar(int codigo) {
        for (OrdemServico ordem : os) {
            if (ordem.getCodigo() == codigo) {
            	os.remove(ordem);
            	os.add(0, ordem);
                return ordem;
            }
        }
        return null;
    }

    public void remover(int codigo) {
    	os.removeIf(ordem -> ordem.getCodigo() == codigo);
    }

    public int contarElementos() {
        return os.size();
    }

    public OrdemServico buscarPorIndice(int index) {
        if (index < 0 || index >= os.size()) {
            return null;
        }
        OrdemServico ordem = os.get(index);
        os.remove(index);
        os.add(0, ordem);
        return ordem;
    }

    public void listarOrdens() {
        for (OrdemServico ordem : os) {
            System.out.println(ordem.imprimir());
        }
    }
}
