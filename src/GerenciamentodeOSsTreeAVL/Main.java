package GerenciamentodeOSsTreeAVL;

public class Main {
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            servidor.carregarDadosIniciais("BancodeDados.txt");

            Cliente cliente = new Cliente(servidor);

            ClienteHandler clienteHandler = new ClienteHandler(cliente);

            clienteHandler.iniciar();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
