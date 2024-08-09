package GerenciamentodeOSs;

public class Main {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        Cliente cliente = new Cliente(servidor);
        ClienteHandler clienteHandler = new ClienteHandler(cliente);
        try {
			clienteHandler.iniciar();
		} catch (Exception e) {

			e.printStackTrace();
		}
    }
}
