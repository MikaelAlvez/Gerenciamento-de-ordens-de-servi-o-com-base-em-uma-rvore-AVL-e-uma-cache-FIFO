import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClienteHandler {
    private Servidor server;
    private Scanner sc;
    private Huffman huffman;

    public ClienteHandler(Servidor server) {
        this.server = server;
        this.sc = new Scanner(System.in);
        huffman = new Huffman();
    }

    public void exibirMenu() {
        boolean option = true;

        while (option) {
            try {
                System.out.println("Selecione a opção desejada:");
                System.out.println("[1] Consultar OS");
                System.out.println("[2] Listar OS");
                System.out.println("[3] Cadastrar OS");
                System.out.println("[4] Editar OS");
                System.out.println("[5] Remover OS");
                System.out.println("[6] Quantidade de registros");
                System.out.println("[0] Sair");
                System.out.print("\nDigite a opção: ");

                int escolha = sc.nextInt();

                switch (escolha) {
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
                        exibirQuantidade();
                        break;
                    /*case 7:
                    	System.out.println("1-Inserções\n2-Remoções\n3-Alterações\n4-Buscas");
                        int escolhaLog = sc.nextInt();
                        
                        Mensagem mensagemLog = new Mensagem(escolhaLog, "Ocorrencias", "", "", "");
                        server.escreverLog("Solicitação ocorrência de log recebida.");
                        server.processarMensagem(mensagemLog);                        
                        break;*/
                    case 0:
                        System.out.println("\nPrograma encerrado...");
                        option = false;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Opção inválida.");
                sc.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Opção inválida.");
                break;
            }
        }
    }

    private void consultarOS() {
        System.out.println("\n----- Consultar OS -----");
        System.out.println("\nInforme o número da OS a ser consultada: ");
        int codBuscar = sc.nextInt();
        Mensagem mensagemBuscar = new Mensagem(codBuscar, "Buscar", "", "", "");
        server.escreverLog("Consultar OS " + codBuscar + " recebido");
        server.processarMensagem(mensagemBuscar);
    }

    private void listarOS() {
        System.out.println("\n----- Lista de OS -----");
        Mensagem mensagemListar = new Mensagem(0, "Listar", "", "", "");
        server.processarMensagem(mensagemListar);
    }

    private void cadastrarOS() {
        System.out.println("\n----- Cadastrar OS -----");
        System.out.println("\nNúmero da OS: ");
        int codInserir = sc.nextInt();
        sc.nextLine();

        OrdensdeServicos osInserir = server.buscarOrdemServico(codInserir, false);

        if (osInserir != null) {
            System.out.println("OS já existe");
            return;
        }

        System.out.println("Nome da OS:");
        String nome = sc.nextLine();
        System.out.println("Descrição da OS:");
        String descricao = sc.nextLine();

        // Inicializa Huffman e comprime os dados
        String[] textos = {nome, descricao};
        huffman.inicializar(textos);
        String nomeComprimido = huffman.comprimir(nome);
        String descricaoComprimida = huffman.comprimir(descricao);

        LocalDateTime horaAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String hora = horaAtual.format(formatter);

        // Cria mensagem com dados comprimidos
        Mensagem mensagem = new Mensagem(codInserir, "Cadastrar", nomeComprimido, descricaoComprimida, hora);

        // Descomprime para exibir os dados descomprimidos no log
        String nomeDescomprimido = huffman.descomprimir(mensagem.descomprimirNome());
        String descricaoDescomprimida = huffman.descomprimir(mensagem.descomprimirDescricao());

        // Log com dados comprimidos
        server.escreverLog("Solicitação de cadastrar OS " + codInserir + " recebida.\n-----Compressão-----\nNome: " + nomeComprimido + "\nDescrição: " + descricaoComprimida);
        
        System.out.println("\nOS cadastrada com sucesso!");
        System.out.println("Nome: " + nomeDescomprimido);
        System.out.println("Descrição: " + descricaoDescomprimida + "\n");
        
        Mensagem mensagemdes = new Mensagem(codInserir, "Cadastrar", nomeDescomprimido, descricaoDescomprimida, hora);

        server.processarMensagem(mensagemdes);
    }

    private void editarOS() {
        System.out.println("\n----- Editar OS -----");
        System.out.println("\nInforme o número da OS a ser editada: ");
        int cod = sc.nextInt();
        sc.nextLine();

        OrdensdeServicos osEdit = server.buscarOrdemServico(cod, false);
        if (osEdit != null) {
            System.out.println(osEdit);

            System.out.println("Novo nome da OS:");
            String nomeEdit = sc.nextLine();
            System.out.println("Nova descrição da OS:");
            String descricaoEdit = sc.nextLine();

            // Inicializa Huffman e comprime os dados
            String[] textosEditados = {nomeEdit, descricaoEdit};
            huffman.inicializar(textosEditados);
            String nomeComprimido = huffman.comprimir(nomeEdit);
            String descricaoComprimida = huffman.comprimir(descricaoEdit);

            LocalDateTime horaAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String hora = horaAtual.format(formatter);

            // Cria mensagem com dados comprimidos
            Mensagem mensagemAlterar = new Mensagem(cod, "Alterar", nomeComprimido, descricaoComprimida, hora);

            // Descomprime para exibir os dados descomprimidos no log
            /*String nomeDescomprimido = huffman.descomprimir(mensagemAlterar.descomprimirNome());
            String descricaoDescomprimida = huffman.descomprimir(mensagemAlterar.descomprimirDescricao());*/

            // Log com dados comprimidos
            server.escreverLog("Solicitação de edição da OS " + cod + " recebida.\n-----Compressão-----\nNome: " + nomeComprimido + "\nDescrição: " + descricaoComprimida);
            
            System.out.println("\nOS editada com sucesso!");
            /*System.out.println("Nome: " + nomeDescomprimido);
            System.out.println("Descrição: " + descricaoDescomprimida + "\n");
            
            Mensagem mensagemdes = new Mensagem(cod, "Cadastrar", nomeDescomprimido, descricaoDescomprimida, hora);*/

            server.processarMensagem(mensagemAlterar);
        } else {
            System.out.println("Ordem de Serviço não encontrada!\n");
        }
    }

    private void removerOS() {
        System.out.println("Informe o número da OS a ser removida:");
        int cod = sc.nextInt();
        sc.nextLine();

        Mensagem mensagemRemover = new Mensagem(cod, "Remover", "", "", "");
        server.escreverLog("Solicitação de remoção da OS " + cod + " recebida.");
        System.out.println("\nOS removida com sucesso!\n");
        server.processarMensagem(mensagemRemover);
    }

    private void exibirQuantidade() {
        Mensagem mensagemQuantidade = new Mensagem(0, "Quantidade", "", "", "");
        server.escreverLog("Solicitação de quantidade de OSs recebida.");
        server.processarMensagem(mensagemQuantidade);
    }
}
