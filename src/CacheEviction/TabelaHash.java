package CacheEviction;
import java.io.FileWriter;
import java.io.IOException;

public class TabelaHash {
    private int tam;
    private ListaAutoajustavel[] tabela; 
    private int tamanho;
    private final double fatorCargaMaximo = 0.75;

    public TabelaHash() {
        tam = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(30));
        tabela = new ListaAutoajustavel[tam];
        tamanho = 0;

        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = new ListaAutoajustavel();
        }
    }

    public void inserir(int codigo, OrdemServico ordem) {
        
        if ((double) tamanho / tabela.length >= fatorCargaMaximo) {
            redimensionar();
        }

        int indice = hash(codigo);
        ListaAutoajustavel lista = tabela[indice];

        lista.adicionar(ordem);
        tamanho++;
    }

    public void remover(int codigo) {
        int indice = hash(codigo);
        ListaAutoajustavel lista = tabela[indice];

        OrdemServico removido = lista.buscar(codigo);
        if (removido != null) {
            lista.remover(codigo);
            tamanho--;

            if ((double) tamanho / tabela.length < 0.25) {
                redimensionarParaMenor();
            }
        }
    }

    public OrdemServico buscar(int codigo) {
        int indice = hash(codigo);
        ListaAutoajustavel lista = tabela[indice];

        return lista.buscar(codigo);
    }

    public void alterarOrdemServico(OrdemServico novaOrdem) {
        int indice = hash(novaOrdem.getCodigo());
        ListaAutoajustavel lista = tabela[indice];

        if (lista.buscar(novaOrdem.getCodigo()) != null) {
            lista.adicionar(novaOrdem);
            System.out.println("Ordem de serviço " + novaOrdem.getCodigo() + " foi alterada.");
        } else {
            System.out.println("Ordem de serviço com código " + novaOrdem.getCodigo() + " não encontrada.");
        }
    }

    private int hash(int codigo) {
        return Integer.hashCode(codigo) % tabela.length;
    }

    private void redimensionar() {
        int novoTamanho = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(tabela.length * 2));
        ListaAutoajustavel[] novaTabela = new ListaAutoajustavel[novoTamanho];
        for (int i = 0; i < novaTabela.length; i++) {
            novaTabela[i] = new ListaAutoajustavel();
        }

        // Re-hash dos elementos da tabela antiga para a nova tabela
        for (ListaAutoajustavel lista : tabela) {
            for (int j = 0; j < lista.contarElementos(); j++) {
                OrdemServico ordem = lista.buscarPorIndice(j); // Usa o novo método
                if (ordem != null) {
                    novaTabela[hash(ordem.getCodigo())].adicionar(ordem);
                }
            }
        }

        tabela = novaTabela;
        tam = novoTamanho;

        escreverLog("Tabela redimensionada para um tamanho maior");
    }

    private void redimensionarParaMenor() {
        int novoTamanho = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(tabela.length / 2));
        ListaAutoajustavel[] novaTabela = new ListaAutoajustavel[novoTamanho];
        for (int i = 0; i < novaTabela.length; i++) {
            novaTabela[i] = new ListaAutoajustavel();
        }

        // Transferindo as ordens de serviço para a nova tabela
        for (ListaAutoajustavel lista : tabela) {
            for (int j = 0; j < lista.contarElementos(); j++) {
                OrdemServico ordem = lista.buscarPorIndice(j); // Usa o novo método
                if (ordem != null) {
                    novaTabela[hash(ordem.getCodigo())].adicionar(ordem);
                }
            }
        }

        tabela = novaTabela;
        tam = novoTamanho;

        escreverLog("Tabela redimensionada para um tamanho menor");
    }

    public void imprimirTabela() {
        for (int i = 0; i < tabela.length; i++) {
        	ListaAutoajustavel lista = tabela[i];
   
            for (int j = 0; j < lista.contarElementos(); j++) {
            	OrdemServico ordem = lista.buscarPorIndice(j);
                	if (ordem != null) {
                		System.out.print("Cód: " +ordem.getCodigo() + 
                				"\nNome: " + ordem.getNome() +
                        		"\nDescrição: " + ordem.getDescricao() +
                        		"\n---------------------------------------\n");
                    }
                }
        }
    }

    public int getTamanho() {
        return this.tamanho;
    }

    public int gettam() {
        return this.tam;
    }

    public float getFatorDeCarga() {
        return (float) tamanho / tabela.length;
    }

    private int proximaPotenciaDe2(int n) {
        int potencia = 1;
        while (potencia < n) {
            potencia *= 2;
        }
        return potencia;
    }

    private int encontrarMaiorPrimoAbaixo(int n) {
        n--; 
        while (!ehPrimo(n)) {
            n--;
        }
        return n;
    }

    private boolean ehPrimo(int n) {
        if (n <= 1)
            return false;
        if (n == 2 || n == 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    public String gerarString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("Tamanho atual: ").append(getTamanho()).append("\n");
        sb.append("Tamanho total: ").append(gettam()).append("\n");
        sb.append("Fator de Carga: ").append(getFatorDeCarga()).append("\n");
        return sb.toString();
    }

    private void escreverLog(String msg) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
