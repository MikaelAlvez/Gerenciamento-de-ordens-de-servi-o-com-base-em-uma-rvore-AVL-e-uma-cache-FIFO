package GerenciamentodeOSsCompressao;

public class Mensagem {
    private String codigoOS;
    private String descricao;
    private String cliente;
    private String operacao;
    private String protocolo;

    public Mensagem(String codigoOS, String descricao, String cliente, String operacao, String protocolo) {
        this.codigoOS = codigoOS;
        this.descricao = descricao;
        this.cliente = cliente;
        this.operacao = operacao;
        this.protocolo = protocolo;
    }

    public String getCodigoOS() {
        return codigoOS;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCliente() {
        return cliente;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getProtocolo() {
        return protocolo;
    }
}

