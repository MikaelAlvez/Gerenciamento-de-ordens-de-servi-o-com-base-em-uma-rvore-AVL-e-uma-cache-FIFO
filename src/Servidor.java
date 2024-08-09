package GerenciamentodeOSs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class Servidor {
  private ArvoreAVL BancodeDados;
  private Cache cache;
  private int hits;
  private int miss;

  public Servidor() {
    this.BancodeDados = new ArvoreAVL();
    this.cache = new Cache();
    this.hits = 0;
    this.miss = 0;
  }

  public OrdensdeServicos buscarOSCache(int cod) throws Exception{
	  OrdensdeServicos ordem = cache.recuperarObjeto(cod); 
    if (ordem != null) {
      hits++;
      return ordem;
    }

    ordem = BancodeDados.buscar(cod);
    if (ordem != null) {
      cache.fifo(ordem);
      miss++;
    }

    return ordem;
  }

  public Boolean cadastrarOS(OrdensdeServicos os) throws Exception{
    int alturaAnt = BancodeDados.getAltura();

    BancodeDados.adicionar(os);

    if (BancodeDados.rotacionado()) {
    	log("Insercao", true, alturaAnt, BancodeDados.getAltura(), os.getCod());
    } else {
    	log("Insercao", false, alturaAnt, BancodeDados.getAltura(), os.getCod());
    }

    return true;
  }

  public List<OrdensdeServicos> listarOs() {
    return BancodeDados.listaOS();
  }

  public Boolean alterarOS(OrdensdeServicos os) throws Exception {
	  BancodeDados.alterar(os);

	  OrdensdeServicos ordem = cache.objetoPresente(os.getCod());

    if (ordem != null) {
      cache.remover(ordem);
      cache.fifo(os);
    }

    return true;
  }

  public Boolean removerOS(int cod) throws Exception {
    int alturaAnt = BancodeDados.getAltura();

    BancodeDados.remover(cod);

    OrdensdeServicos ordem = cache.objetoPresente(cod);

    if (ordem != null) {
      cache.remover(ordem);
    }

    if (BancodeDados.rotacionado()) {
    	log("Remocao", true, alturaAnt, BancodeDados.getAltura(), cod);
    } else {
    	log("Remocao", false, alturaAnt, BancodeDados.getAltura(), cod);
    }

    return true;
  }

  public int getRegistros() {
    return BancodeDados.getRegistros();
  }

  private void log(String funcoes, boolean rotacao, int alturaAnt, int novaAlt, int cod) {
	  String balanceamento;
	  if (rotacao) {
	      balanceamento = "Rotação realizada!";
	  }else {
		  balanceamento = "Árvore balanceada, não precisou rotacionar";
	  }
	  
    String message = String.format(
    		"\nAltura inicial: %d\n" +
            "Função realizada: %s do Node %d\n" +
            "Balanceamento: %s\n" +
            "Altura atual: %d\n",
            alturaAnt, funcoes, cod, balanceamento, novaAlt);

    try (FileWriter escreverArq = new FileWriter("log.txt", true);
        BufferedWriter escreverBuf = new BufferedWriter(escreverArq);
        PrintWriter exibir = new PrintWriter(escreverBuf)) {
    	exibir.println(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArvoreAVL getBancodeDados() {
    return BancodeDados;
  }

  public Cache getCache() {
    return cache;
  }

  public int getHits() {
    return hits;
  }

  public int getMiss() {
    return miss;
  }
}