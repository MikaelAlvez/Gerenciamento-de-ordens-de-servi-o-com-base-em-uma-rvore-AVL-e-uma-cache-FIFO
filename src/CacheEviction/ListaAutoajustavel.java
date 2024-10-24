import java.util.ArrayList;
import java.util.List;

public class ListaAutoajustavel {
    private List<OrdensdeServicos> os;

    public ListaAutoajustavel() {
        this.os = new ArrayList<>();
    }

    public void adicionar(OrdensdeServicos ordem) {
    	os.add(0, ordem);
    }

    public OrdensdeServicos buscar(int codigo) {
        for (OrdensdeServicos ordem : os) {
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

    public OrdensdeServicos buscarPorIndice(int index) {
        if (index < 0 || index >= os.size()) {
            return null;
        }
        OrdensdeServicos ordem = os.get(index);
        os.remove(index);
        os.add(0, ordem);
        return ordem;
    }

    public void listarOrdens() {
        for (OrdensdeServicos ordem : os) {
            System.out.println(ordem.imprimir());
        }
    }
}
