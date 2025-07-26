import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        try (Scanner leitor = new Scanner(System.in)) {
            System.out.println("\n --- Compilador --- \n");
            System.out.print("Digite o nome ou caminho para o arquivo fonte do programa.........");
            String caminho = leitor.nextLine();
            
            Sintax analisador_sintatico = new Sintax(caminho);
            try{
                System.out.println("\nInicilizando a análise.......");
                System.out.println(analisador_sintatico.analyse());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
