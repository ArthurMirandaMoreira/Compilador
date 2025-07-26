
import java.util.ArrayList;
import java.util.HashMap;

public class SymbTable {

    // tabela de símbolos é uma lista de tabelas, para suportar diferentes escopos
    private ArrayList<HashMap<String, String>> tabelas; // hashmap com Nome e Tipo
    private int escopo;

    public SymbTable(){
        escopo = 0;
        tabelas = new ArrayList<HashMap<String, String>>();
    }

    // devolve a tabela de símbolos
    public ArrayList<HashMap<String, String>> getTables() {
        return tabelas;
    }

    // cria um novo "bloco", usar quando entrar em novo escopo
    public void addNewBlock() {
        // cria uma nova tabela e atualiza o escopo
        HashMap<String, String> tabela = new HashMap<String, String>();
        tabelas.add(tabela);
        escopo += 1;
    }

    // remove o último "bloco", usar quando sair do escopo
    public void removeLastBlock() throws SymbTableException {
        if (escopo > 0) {
            // se há pelo menos 1 tabela, remove a última tabela e atualiza o escopo    
            tabelas.removeLast();
            escopo -= 1;
        } else {
            throw new SymbTableException("Tabela de símbolos não inicializada.");
        }
    }

    // procura uma variavel em todas os blocos, começando do bloco mais interno
    public String searchWord(Word w) {
        String tipo = null;
        for (int i = escopo - 1; i >= 0; i--) {                     // itera cada tabela, começando da mais recente
            HashMap<String, String> tabela = tabelas.get(i);     // coleta a tabela mais recente
            tipo = tabela.get(w.toString());                   // tenta achar a palavra nela
            // verifica se encontrou, se sim, retorna
            if (tipo != null) {
                return tipo;
            }
        }
        // se não encontrar em nenhuma das tabelas, retorna null
        return null;
    }

    // procura uma variavel apenas na última tabela
    // é usada nas declarações: pode-se ter duas variaveis com mesmo nome, desde que em escopos diferentes
    public String searchWordInLastTable(Word w) {
        HashMap<String, String> tabela = tabelas.getLast();
        String tipo = tabela.get(w.toString());
        return tipo;
    }

    // insere uma palavra no bloco mais recente
    public void insertWord(Word w, String tipo) {
        // insere a palavra na tabela mais recente
        HashMap<String, String> tabela = tabelas.getLast();
        tabela.put(w.toString(), tipo);
        // pouco eficiente, mas remove o último e adiciona a versão atualizada dele
        tabelas.removeLast();
        tabelas.add(tabela);
    }
}
