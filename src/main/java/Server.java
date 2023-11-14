import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<Socket, String> clientMap = new HashMap<>();
    private static Map<String, List<String>> temasPalavras = new HashMap<>();
    private static Map<String, String> palavraOriginalMap = new HashMap<>();

    public static void main(String[] args) {
        // Adicione palavras aos temas conforme necessário
        List<String> palavrasAnimais = Arrays.asList("Elefante", "Girafa", "Leão", "Macaco");
        List<String> palavrasCores = Arrays.asList("Vermelho", "Azul", "Verde", "Amarelo");
        List<String> palavrasFrutas = Arrays.asList("Maçã", "Banana", "Uva", "Morango");

        temasPalavras.put("Animais", palavrasAnimais);
        temasPalavras.put("Cores", palavrasCores);
        temasPalavras.put("Frutas", palavrasFrutas);

        try {
            // Criação do servidor socket na porta 12345
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Servidor esperando por conexões...");

            while (true) {
                // Aguardar a conexão de um cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Criar uma nova thread para o cliente
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            // Obter streams de entrada e saída do cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);

            // Receber o nome do cliente
            String nomeCliente = in.readLine();
            System.out.println("Cliente '" + nomeCliente + "' identificado.");

            // Associar o nome do cliente ao socket
            clientMap.put(clientSocket, nomeCliente);

            // Loop para troca de mensagens
            while (true) {
                String clientMessage = in.readLine();
                System.out.println(nomeCliente + ": " + clientMessage);

                if (clientMessage.startsWith("TEMADOJOGO:")) {
                    // Extrair o tema escolhido
                    String temaEscolhido = clientMessage.substring("TEMADOJOGO:".length());

                    // Obter uma palavra aleatória do tema escolhido
                    String palavraAleatoria = obterPalavraAleatoria(temaEscolhido);

                    // Armazenar a palavra original no mapa para posterior verificação
                    palavraOriginalMap.put(nomeCliente, palavraAleatoria);

                    // Enviar a palavra aleatória ao cliente
                    out.println(palavraAleatoria);
                } else if (clientMessage.startsWith("ADIVINHARPALAVRA:")) {
                    // Extrair a tentativa de adivinhar
                    String tentativa = clientMessage.substring("ADIVINHARPALAVRA:".length());

                    // Verificar se a tentativa é correta
                    String palavraOriginal = palavraOriginalMap.get(nomeCliente);
                    if (palavraOriginal != null && tentativa.equalsIgnoreCase(palavraOriginal)) {
                        out.println("Parabéns! Você acertou a palavra.");
                    } else {
                        out.println("Palavra incorreta. Tente novamente.");
                    }
                }

                // Verificar se o cliente quer encerrar a conexão
                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            // Remover o cliente ao encerrar a conexão
            clientMap.remove(clientSocket);

            // Fechar conexões
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String obterPalavraAleatoria(String tema) {
        List<String> palavras = temasPalavras.get(tema);
        if (palavras != null && !palavras.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(palavras.size());
            return palavras.get(index);
        } else {
            return "Sem palavras para o tema '" + tema + "'";
        }
    }
}
