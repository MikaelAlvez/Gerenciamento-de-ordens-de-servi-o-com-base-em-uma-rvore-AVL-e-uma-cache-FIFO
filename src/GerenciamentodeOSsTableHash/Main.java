package GerenciamentodeOSsTableHash;

public class Main {
    public static void main(String[] args) {
        try {
            // Criar uma instância do Servidor
            Servidor servidor = new Servidor();

            // Inicializar o manipulador de servidor
            ServidorHandler servidorHandler = new ServidorHandler(servidor);

            // Iniciar o loop de interação com o usuário
            servidorHandler.iniciar();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o sistema: " + e.getMessage());
        }
    }
}
