package GerenciadordeOSCompressao;

import GerenciamentodeOSsTableHash.Cliente;
import GerenciamentodeOSsTableHash.ClienteHandler;
import GerenciamentodeOSsTableHash.Servidor;

public class Main {
    public static void main(String[] args) {
        try {
            // Criar uma instância do Servidor
            Servidor servidor = new Servidor();

            // Criar uma instância do Cliente, passando a referência do Servidor
            Cliente cliente = new Cliente(servidor);

            // Inicializar o manipulador de cliente
            ClienteHandler clienteHandler = new ClienteHandler(cliente);

            // Iniciar o loop de interação com o usuário
            clienteHandler.iniciar();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o sistema: " + e.getMessage());
        }
    }
}
