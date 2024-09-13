package GerenciamentodeOSsTreeAVL;

import java.util.List;
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
                    consultarOS();
                    break;
                case 2:
                    listarOS();
                    break;
                case 3:
                    cadastrarOS();
                    break;
                case 4:
                    editarOS();
                    break;
                case 5:
                    removerOS();
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

    private void consultarOS() throws Exception {
        System.out.print("\nInforme o número da OS a ser consultada: ");
        int codigo = scanner.nextInt();
        OrdensdeServicos os = cliente.BuscarOS(codigo);

        if (os != null) {
            System.out.println("\nOrdem de Serviço encontrada!\n");
            System.out.println("Código: " + os.getCod());
            System.out.println("Nome: " + os.getNome());
            System.out.println("Descrição: " + os.getDescricao());
            System.out.println("Horário: " + os.getHorario() + "\n");
        } else {
            System.out.println("OS com código " + codigo + " não encontrada.");
        }
    }

    private void listarOS() {
        System.out.println("\n----- Lista de OS -----");
        List<OrdensdeServicos> ordens = cliente.ListarOS();

        if (ordens.isEmpty()) {
            System.out.println("Nenhuma OS cadastrada.");
        } else {
            for (OrdensdeServicos os : ordens) {
                System.out.println("\nCód: " + os.getCod() 
                + "\nNome: " + os.getNome() 
                + "\nDescrição: " + os.getDescricao() 
                + "\nHorário/data: " + os.getHorario());
            }
        }
        System.out.println("-----------------------");
    }

    private void cadastrarOS() {
        System.out.print("\n----- Cadastrar OS -----\n");

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
                System.out.println("\nOrdem de Serviço cadastrada com sucesso!");
            } else {
                System.out.println("\nFalha ao cadastrar a OS.");
            }
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar OS: " + e.getMessage());
        }
    }

    private void editarOS() throws Exception {
        System.out.print("\n----- Editar OS -----\n");

        System.out.print("\nInforme o número da OS a ser editada: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();

        OrdensdeServicos osExistente = cliente.BuscarOS(codigo);

        if (osExistente != null) {
            System.out.print("Novo nome da OS: ");
            String novoNome = scanner.nextLine();

            System.out.print("Nova descrição da OS: ");
            String novaDescricao = scanner.nextLine();

            osExistente.setNome(novoNome);
            osExistente.setDescricao(novaDescricao);

            if (cliente.AlterarOS(osExistente)) {
                System.out.println("\nOS atualizada com sucesso!");
            } else {
                System.out.println("\nFalha ao atualizar a OS.");
            }
        } else {
            System.out.println("\nOS com código " + codigo + " não encontrada.");
        }
    }

    private void removerOS() throws Exception {
        System.out.print("\nInforme o número da OS a ser removida: ");
        int codigo = scanner.nextInt();

        if (cliente.RemoverOS(codigo)) {
            System.out.println("OS removida com sucesso!");
        } else {
            System.out.println("Falha ao remover a OS.");
        }
    }

    private void sair() {
        System.out.println("Saindo...");
        scanner.close();
    }
}
