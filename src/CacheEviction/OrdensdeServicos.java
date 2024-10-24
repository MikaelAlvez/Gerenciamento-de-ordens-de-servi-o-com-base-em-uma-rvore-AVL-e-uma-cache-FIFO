import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrdensdeServicos {
    private int cod;
    private String nome;
    private String descricao;
    private LocalDateTime horario;
    
    public OrdensdeServicos(int codigo, String nome, String descricao, String horario) {
    	this.cod = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.horario = LocalDateTime.now();
    }

    public int getCodigo() {
        return cod;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCodigo(int codigo) {
        this.cod = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public LocalDateTime format(DateTimeFormatter isoLocalDateTime) {
    	return horario;
    }
    
    public String toString() {
        return String.format(
        	"%n-----------------------------------------------%n" +
            "Código: %d%n" +
            "Nome: %-15s%n" +
            "Descrição: %-15s%n" +
            "Horário: %s %n" +
            "-----------------------------------------------%n",
            cod, nome, descricao, horario
        );
    }

    public String imprimir() {
        return String.format("Cód: %d\n", cod);
    }

}
