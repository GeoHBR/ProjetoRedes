import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<Socket, String> clientMap = new HashMap<>();
    private static Map<String, List<String>> temasPalavras = new HashMap<>();

    public static void main(String[] args) {
        // Lista de palavras dos temas
        List<String> palavrasAnimais = Arrays.asList("Elefante", "Girafa", "Leão", "Macaco");
        List<String> palavrasCores = Arrays.asList("Vermelho", "Azul", "Verde", "Amarelo");
        List<String> palavrasFrutas = Arrays.asList("Maçã", "Banana", "Uva", "Morango");

        temasPalavras.put("Animais", palavrasAnimais);
        temasPalavras.put("Cores", palavrasCores);
        temasPalavras.put("Frutas", palavrasFrutas);

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor esperando por conexões...");

            while (true) {
                // Esperando um Client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Cria uma nova thread para o cliente
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

            //Nome do cliente
            String nomeCliente = in.readLine();
            System.out.println("Cliente '" + nomeCliente + "' identificado.");

            // Associa nome do cleint com o socket
            clientMap.put(clientSocket, nomeCliente);

            // Loop das msgs
            while (true) {
                String clientMessage = in.readLine();
                System.out.println(nomeCliente + ": " + clientMessage);

                if (clientMessage.startsWith("TEMADOJOGO:")) {

                    String temaEscolhido = clientMessage.substring("TEMADOJOGO:".length());

                    // Pega a palavra
                    String palavraAleatoria = obterPalavraAleatoria(temaEscolhido);

                    // Envia a palavra aleatoria pro client
                    out.println(palavraAleatoria);
                } else {
                    /* Se a mensagem não começar com "TEMADOJOGO:", considera como a mensagem escrita pelo cliente,
                    botei isso pq primeiro chega pro server a resposta do tema, e ele tratava como se fosse a msg escrita pelo client*/
                    System.out.println("Mensagem escrita por " + nomeCliente + ": " + clientMessage);
                }
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
