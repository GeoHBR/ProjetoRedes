import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        try {
            String IpServer = JOptionPane.showInputDialog("Digite o IP do servidor padrão local: localhost");

            // Conectar ao servidor na porta 12345
            Socket socket = new Socket(IpServer, 12345);

            // Obter streams de entrada e saída do cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            // Solicitar ao usuário que digite seu nome
            String nomeCliente = JOptionPane.showInputDialog("Digite seu nome:");

            // Enviar nome ao servidor
            out.println(nomeCliente);

            // Loop principal do cliente
            while (true) {
                String opcao = JOptionPane.showInputDialog("Selecione uma Opção:\n1-Escolher Palavra\n2-Adivinhar Palavra\n3-Sair");

                if (opcao.equals("1")) {
                    // Solicitar ao usuário que escolha um tema
                    String[] temas = {"Animais", "Cores", "Frutas"}; // Adicione mais temas conforme necessário
                    String temaEscolhido = (String) JOptionPane.showInputDialog(null, "Escolha um tema:", "Escolha de Tema", JOptionPane.PLAIN_MESSAGE, null, temas, temas[0]);

                    // Solicitar a palavra aleatória do tema escolhido
                    out.println("TEMADOJOGO:" + temaEscolhido);

                    // Receber resposta do servidor (palavra aleatória)
                    String palavraAleatoria = in.readLine();
                    JOptionPane.showMessageDialog(null, "Palavra do tema '" + temaEscolhido + "': " + palavraAleatoria);

                    // Continue com o jogo normalmente (envie uma mensagem ao servidor)
                    String message = JOptionPane.showInputDialog("Digite uma mensagem para o servidor:");

                    // Verifique se o usuário inseriu uma palavra antes de enviar ao servidor
                    if (message != null && !message.trim().isEmpty()) {
                        out.println("ESCREVERPALAVRA:" + message);
                    } else {
                        JOptionPane.showMessageDialog(null, "Você não digitou uma palavra. Tente novamente.");
                        //continue; // Volte ao início do loop
                    }
                } else if (opcao.equals("2")) {
                    // Solicitar ao usuário que adivinhe a palavra
                    String tentativa = JOptionPane.showInputDialog("Digite sua tentativa:");

                    // Enviar tentativa ao servidor
                    out.println("ADIVINHARPALAVRA:" + tentativa);

                    // Receber resposta do servidor e exibir usando JOptionPane
                    String serverResponse = in.readLine();
                    JOptionPane.showMessageDialog(null, "Resposta do Servidor:\n" + serverResponse);
                } else if (opcao.equals("3")) {
                    // Solicitar ao usuário se deseja realmente sair
                    int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirmacao == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Conexão Encerrada");
                        // Fechar conexões e sair
                        in.close();
                        out.close();
                        socket.close();
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro na conexão com o servidor");
        }
    }
}
