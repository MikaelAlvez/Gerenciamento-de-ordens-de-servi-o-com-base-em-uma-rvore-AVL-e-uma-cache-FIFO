
public class Mensagem {
    private int cod;
    private String nome;
    private String descricao;
    private String hora;
    private String operacao;
    private Huffman huffman;

    public Mensagem(int cod, String operacao, String nome, String descricao, String hora) {
        this.cod = cod;
        this.huffman = new Huffman();
        
        // Inicializa o Huffman com os textos que serão comprimidos
        String[] textos = {nome, descricao, hora, operacao};
        huffman.inicializar(textos);

        // Comprime os dados
        this.nome = huffman.comprimir(nome);
        this.descricao = huffman.comprimir(descricao);
        this.hora = huffman.comprimir(hora);
        this.operacao = huffman.comprimir(operacao);
    }

    // Métodos para descomprimir os dados
    public String descomprimirNome() {
        return huffman.descomprimir(nome);
    }

    public String descomprimirDescricao() {
        return huffman.descomprimir(descricao);
    }

    public String descomprimirHora() {
        return huffman.descomprimir(hora);
    }

    public String descomprimirOperacao() {
        return huffman.descomprimir(operacao);
    }

    // Getter para o código
    public int getCod() {
        return this.cod;
    }

    // Método para descomprimir todos os campos em uma string formatada
    public String descomprimirTodos() {
        return String.format("Código: %d, Nome: %s, Descrição: %s, Hora: %s, Operação: %s",
                cod, descomprimirNome(), descomprimirDescricao(), descomprimirHora(), descomprimirOperacao());
    }
}
