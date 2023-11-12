import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class Client {
    public static void main(String[] args) {
        try {
            while (true) {

                String IpServer = JOptionPane.showInputDialog("Digite o IP do servidor, padrão local: localhost:");


                Socket socket = new Socket(IpServer, 12345);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

                String nomeCliente = JOptionPane.showInputDialog("Digite seu nome:");
                out.println(nomeCliente);

                // Loop pra enviar palavras
                while (true) {
                    String opcao = JOptionPane.showInputDialog("Escolha uma opção:\n1-Escolher Palavra\n2-Adivinhar Palavra\n3-Sair");

                    if (opcao.equals("1")) {
                        String[] temas = {"Animais", "Cores", "Frutas"};
                        String temaEscolhido = (String) JOptionPane.showInputDialog(null, "Escolha um tema:", "Escolha de Tema", JOptionPane.PLAIN_MESSAGE, null, temas, temas[0]);

                        out.println("TEMADOJOGO:" + temaEscolhido);


                        String palavraAleatoria = in.readLine();
                        JOptionPane.showMessageDialog(null, "Palavra do tema '" + temaEscolhido + "': " + palavraAleatoria);


                        String message = JOptionPane.showInputDialog("Digite uma mensagem para o servidor:");

                        // Verifica se o user escreveu algo
                        if (message != null && !message.trim().isEmpty()) {
                            out.println(message);
                        } else {
                            JOptionPane.showMessageDialog(null, "Você não digitou uma palavra. Tente novamente.");
                            continue; // Volta ao início do loop
                        }
                    }

                    // Opção de encerrar conexão
                    if (opcao.equals("3")) {
                        JOptionPane.showMessageDialog(null, "Conexão Encerrada");
                        in.close();
                        out.close();
                        socket.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro na conexão com o servidor");
        }
    }
}
