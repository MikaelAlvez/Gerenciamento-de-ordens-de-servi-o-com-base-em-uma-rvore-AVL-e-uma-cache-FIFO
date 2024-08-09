package GerenciamentodeOSs;

import java.util.Scanner;

public class ClienteHandler {
    private Cliente cliente;
    private Scanner scanner;

    public ClienteHandler(Cliente cliente) {
        this.cliente = cliente;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() throws Exception {
        while (true) {
            exibirMenu();
            int opcao = obterOpcao();

            switch (opcao) {
                case 1:
                    System.out.print("\nInforme o número da OS a ser consultada: ");
                    int buscar = scanner.nextInt();
                	cliente.BuscarOS(buscar);
                	break;
                	
                case 2:
                    System.out.print("\n----- Lista de OS -----\n");
                	cliente.ListarOS();
                    System.out.print("\n-----------------------\n");
                    break;
                    
                case 3:
                	System.out.print("\n----- Cadastrar de OS -----\n");

                	System.out.print("\nNúmero da OS: ");
                	int num = scanner.nextInt();
                	scanner.nextLine();

                	System.out.print("Nome da OS: ");
                	String nome = scanner.nextLine();

                	System.out.print("Descrição da OS: ");
                	String descricao = scanner.nextLine();

                	OrdensdeServicos os = new OrdensdeServicos(num, nome, descricao);

                	try {
                	    if (cliente.CadastrarOS(os)) {
                	        System.out.println("\nOS cadastrada com sucesso!");
                	    } else {
                	        System.out.println("\nFalha ao cadastrar a OS.");
                	    }
                	} catch (Exception e) {
                	    System.out.println("\nErro ao cadastrar OS: " + e.getMessage());
                	}

                    
                	cliente.CadastrarOS(new OrdensdeServicos(num, nome, descricao));
                    break;
                    
                case 4:
                	System.out.print("\n----- Editar OS -----\n");

                	System.out.print("\nNovo número da OS: ");
                	int novoNum = scanner.nextInt();
                	scanner.nextLine();

                	System.out.print("Novo nome da OS: ");
                	String novoNome = scanner.nextLine();

                	System.out.print("Nova descrição da OS: ");
                	String novaDescricao = scanner.nextLine();

                	try {
                	    OrdensdeServicos osExistente = cliente.BuscarOS(novoNum);
                	    if (osExistente != null) {
                	        osExistente.setNome(novoNome);
                	        osExistente.setDescricao(novaDescricao);
                	        
                	        if (cliente.AlterarOS(osExistente)) {
                	            System.out.println("\nOS atualizada com sucesso!");
                	        } else {
                	            System.out.println("\nFalha ao atualizar a OS.");
                	        }
                	    } else {
                	        System.out.println("\nOS não encontrada.");
                	    }
                	} catch (Exception e) {
                	    System.out.println("\nErro ao editar OS: " + e.getMessage());
                	}

                	cliente.AlterarOS(new OrdensdeServicos(novoNum, novoNome, novaDescricao));
                    break;
                    
                case 5:
                	System.out.print("\nInforme o número da OS a ser removida: ");
                    int rem = scanner.nextInt();
                	cliente.RemoverOS(rem);
                    break;
                case 6:
                    sair();
                    return;
                default:
                    System.out.println("Opção inválida! Por favor, tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\nSelecione a opção desejada:");
        System.out.println("[1] Consultar OS");
        System.out.println("[2] Listar OS");
        System.out.println("[3] Cadastrar OS");
        System.out.println("[4] Editar OS");
        System.out.println("[5] Apagar OS");
        System.out.println("[6] Sair");
    }

    private int obterOpcao() {
        System.out.print("\nDigite a opção: ");
        return scanner.nextInt();
    }

    private void sair() {
        System.out.println("Saindo...");
    }
}
