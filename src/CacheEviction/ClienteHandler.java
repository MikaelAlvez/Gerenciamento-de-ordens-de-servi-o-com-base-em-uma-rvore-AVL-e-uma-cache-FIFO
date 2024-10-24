package CacheEviction;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

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

        OrdemServico osInserir = server.buscarOrdemServico(codInserir, false);

        if (osInserir != null) {
            System.out.println("OS já existe");
            return;
        }

        System.out.println("Nome da OS:");
        String nome = sc.nextLine();
        System.out.println("Descrição da OS:");
        String descricao = sc.nextLine();

     // Inicializa a árvore de Huffman com o texto
        String[] textos = {nome, descricao}; // Pode incluir mais textos se necessário
        huffman.inicializar(textos);

        // Comprime o nome e a descrição
        String nomeComprimido = huffman.comprimir(nome);
        String descricaoComprimida = huffman.comprimir(descricao);

        LocalDateTime horaAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String hora = horaAtual.format(formatter);

        // Cria a mensagem com os dados comprimidos
        Mensagem mensagem = new Mensagem(codInserir, "Cadastrar", nomeComprimido, descricaoComprimida, hora);

        server.escreverLog("Solicitação de cadastrar OS " + codInserir + " recebida");
        System.out.println("\nOS cadastrada com sucesso!\n");
        server.processarMensagem(mensagem);
        
        server.escreverLog("Processando mensagem:");
        server.escreverLog("Cod: " + codInserir);
        server.escreverLog("Nome comprimido: " + mensagem.descomprimirNome());
        server.escreverLog("Descrição comprimida: " + mensagem.descomprimirDescricao());
        server.escreverLog("Hora comprimida: " + mensagem.descomprimirHora());
        server.escreverLog("Operação comprimida: " + mensagem.descomprimirOperacao());
    }


    private void editarOS() {
        System.out.println("\n----- Editar OS -----");
        System.out.println("\nInforme o número da OS a ser editada: ");
        int cod = sc.nextInt();
        sc.nextLine();

        OrdemServico osEdit = server.buscarOrdemServico(cod, false);
        if (osEdit != null) {
            System.out.println(osEdit);

            System.out.println("Novo nome da OS:");
            String nomeEdit = sc.nextLine();
            System.out.println("Nova descrição da OS:");
            String descricaoEdit = sc.nextLine();

            // Inicializa a árvore de Huffman com o texto editado
            String[] textosEditados = {nomeEdit, descricaoEdit}; // Pode incluir mais textos se necessário
            huffman.inicializar(textosEditados);

            // Comprime o nome e a descrição usando Huffman
            String nomeComprimido = huffman.comprimir(nomeEdit);
            String descricaoComprimida = huffman.comprimir(descricaoEdit);

            LocalDateTime horaAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String hora = horaAtual.format(formatter);

            Mensagem mensagemAlterar = new Mensagem(cod, "Alterar", nomeComprimido, descricaoComprimida, hora);
            server.escreverLog("Solicitação de edição da OS " + cod + " recebida.");
            System.out.println("\nOS editada com sucesso!\n");
            server.processarMensagem(mensagemAlterar);
            
            server.escreverLog("Processando mensagem:");
            server.escreverLog("Cod: " + cod);
            server.escreverLog("Nome comprimido: " + mensagemAlterar.descomprimirNome());
            server.escreverLog("Descrição comprimida: " + mensagemAlterar.descomprimirDescricao());
            server.escreverLog("Hora comprimida: " + mensagemAlterar.descomprimirHora());
            server.escreverLog("Operação comprimida: " + mensagemAlterar.descomprimirOperacao());
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
