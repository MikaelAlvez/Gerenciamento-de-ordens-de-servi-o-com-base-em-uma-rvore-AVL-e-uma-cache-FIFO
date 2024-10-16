package GerenciadordeOSCompressao;

public class Mensagem {
    private OrdensdeServicos os;
    private String operacao;  
    private String opcional;

    public Mensagem(OrdensdeServicos os, String operacao, String opcional) {
        this.os = os;
        this.operacao = operacao;
        this.opcional = opcional;
    }

    public OrdensdeServicos getOs() {
        return os;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getOpcional() {
        return opcional;
    }

    @Override
    public String toString() {
        return "Mensagem{" +
                "os=" + os +
                ", operacao='" + operacao + '\'' +
                ", opcional='" + opcional + '\'' +
                '}';
    }
}
