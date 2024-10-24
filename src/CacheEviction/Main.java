package CacheEviction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Servidor server = new Servidor();
        carregarOrdensDoArquivo(server, "BancodeDados.txt");

        ClienteHandler clienteHandler = new ClienteHandler(server);
        clienteHandler.exibirMenu();
    }

    public static void carregarOrdensDoArquivo(Servidor server, String caminhoArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");

                if (partes.length == 4) {
                    int codigo = Integer.parseInt(partes[0].trim());
                    String nome = partes[1].trim();
                    String descricao = partes[2].trim();
                    String hora = partes[3].trim();

                    Mensagem mensagem = new Mensagem(codigo, "Cadastrar", nome, descricao, hora);
                    server.processarMensagem(mensagem);
                }
            }
            server.escreverLog("Ordens de serviço carregadas do arquivo.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }
}
