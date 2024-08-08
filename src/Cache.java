package GerenciamentodeOSs;

import java.util.ArrayList;

public class Cache {
  private ArrayList<OrdensdeServicos> cache;
  private final int tamanhoCache = 20;

  public Cache() {
    this.cache = new ArrayList<>();
  }

  public void exibirCache() {
    if (cache.isEmpty()) {
      System.out.println("Memória cache vazia!");
      return;
    }
    for (OrdensdeServicos os : cache) {
      System.out.println("\nCódigo: " + os.getCodigo() + 
                         "\nNome: " + os.getNome() + 
                         "\nDescrição: " + os.getDescricao() +
                         "\nHorário da solicitação: " + os.getHorario() + "\n");
    }
  }

  public void fifo(OrdensdeServicos os) {
    if (cache.size() >= tamanhoCache) {
      cache.remove(0);
    }
    cache.add(os);
  }

  public void remover(OrdensdeServicos os) {
    cache.remove(os);
  }

  public OrdensdeServicos recuperarObjeto(int cod) {
    for (OrdensdeServicos os : cache) {
      if (os.getCodigo() == cod) {
        return os;
      }
    }
    return null;
  }

  public OrdensdeServicos objetoPresente(int cod) {
    for (OrdensdeServicos os : cache) {
      if (os.getCodigo() == cod) {
        return os;
      }
    }
    return null;
  }
}
