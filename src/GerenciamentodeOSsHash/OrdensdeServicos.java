package GerenciamentodeOSsHash;

import java.time.LocalDateTime;

public class OrdensdeServicos {
  private final int cod;
  private String nome;
  private String descricao;
  private LocalDateTime horario;

  private static int cont = 0;
  
  public OrdensdeServicos(Integer codigo, String nome, String descricao) {
    if (codigo != null) {
      if (codigo > cont) {
        cont = codigo;
      }
      this.cod = codigo;
    } else {
      this.cod = cont++;
    }
    this.nome = nome;
    this.descricao = descricao;
    this.horario = LocalDateTime.now();
  }

  public int getCod() {
    return cod;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public LocalDateTime getHorario() {
    return horario;
  }

  public void setHorario(LocalDateTime horario) {
    this.horario = horario;
  }
}
