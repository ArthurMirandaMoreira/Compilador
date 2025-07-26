
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sintax {

    private Token tok;
    private Lexer analisador_lexico;
    private SymbTable tabela_simbolos;

    // construtor
    public Sintax(String caminho) throws FileNotFoundException {
        try {
            analisador_lexico = new Lexer(caminho);
            tok = analisador_lexico.nextToken();
            tabela_simbolos = new SymbTable();

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
        } catch (LexicalException e) {
        }
    }

    public SymbTable getTable() {
        return tabela_simbolos;
    }

    // métodos básicos para casar os tokens
    private void advance() throws LexicalException {
        try {
            if (tok != null) {
                // trecho de código para ler os tokens um por um, foi usado durante os testes
                // String obtido = Tag.traduzir_tag(tok.tag);
                // System.out.println("Token é : " + obtido);
                tok = analisador_lexico.nextToken();
            }
        } catch (IOException ex) {
        }
    }

    private void eat(int t) throws SintaxException, LexicalException, SemanticException, SymbTableException, LexicalException {
        if (tok.tag == t) {
            advance();
        } else {
            String esperado = Tag.traduzir_tag(t);
            String obtido = Tag.traduzir_tag(tok.tag);
            throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado token " + t + " ('" + esperado + "'), lido " + tok.tag + " ('" + obtido + "')");
        }
    }

    // método para começar a análise sintática
    public String analyse() throws SintaxException, LexicalException, SemanticException, SymbTableException, SymbTableException {
        program();
        return "Programa bem formado.";
    }

    // métodos do analisador sintático descendente:
    void program() throws SintaxException, LexicalException, SemanticException, SymbTableException, SymbTableException {   //ok
        eat(Tag.PROGRAM);
        tabela_simbolos.addNewBlock();
        decl_list();
        eat(Tag.BEGIN);
        stmt_list();
        eat(Tag.END);
        tabela_simbolos.removeLastBlock();
        eat(Tag.END_OF_FILE);
    }

    void decl_list() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        decl();
        decl_list_sufix();
    }

    void decl_list_sufix() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        switch (tok.tag) {
            case Tag.INT:
                decl();
                decl_list_sufix();
                break;
            case Tag.FLOAT:
                decl();
                decl_list_sufix();
                break;
            case Tag.CHAR:
                decl();
                decl_list_sufix();
                break;
        }
    }

    void decl() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        Node n;
        n = type();
        eat(Tag.COLON);
        ident_list(n);
        eat(Tag.SEMICOLON);
    }

    void ident_list(Node n) throws SintaxException, LexicalException, SemanticException, SymbTableException, SemanticException {   //ok
        Word w = (Word) tok;
        eat(Tag.ID);
        if (tabela_simbolos.searchWordInLastTable(w) == null) {
            tabela_simbolos.insertWord(w, n.type);
        } else {
            throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". Variável " + w.toString() + " declarada duas vezes.");
        }

        if (tok.tag == Tag.COMMA) {
            eat(Tag.COMMA);
            ident_list(n);
        }
    }

    Node type() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok?
        Node n;
        switch (tok.tag) {
            case Tag.INT:
                eat(Tag.INT);
                n = new Node("inteiro");
                return n;
            case Tag.FLOAT:
                eat(Tag.FLOAT);
                n = new Node("real");
                return n;
            case Tag.CHAR:
                eat(Tag.CHAR);
                n = new Node("char");
                return n;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado token {int, float, char}, encontrado " + tok.toString() + " (" + obtido + ")");
        }
    }

    void stmt_list() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        stmt();
        stmt_list_sufix();
    }

    void stmt_list_sufix() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        switch (tok.tag) {
            case Tag.IN:
                stmt();
                stmt_list_sufix();
                break;
            case Tag.OUT:
                stmt();
                stmt_list_sufix();
                break;
            case Tag.IF:
                stmt();
                stmt_list_sufix();
                break;
            case Tag.WHILE:
                stmt();
                stmt_list_sufix();
                break;
            case Tag.REPEAT:
                stmt();
                stmt_list_sufix();
                break;
            case Tag.ID:
                Word w = (Word) tok;
                if (tabela_simbolos.searchWord(w) == null) {
                    throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". Variável " + w.toString() + " não declarada.");
                }
                stmt();
                stmt_list_sufix();
                break;
        }
    }

    void stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException {  //ok
        switch (tok.tag) {
            case Tag.IN:
                read_stmt();
                break;
            case Tag.OUT:
                write_stmt();
                break;
            case Tag.IF:
                if_stmt();
                break;
            case Tag.WHILE:
                while_stmt();
                break;
            case Tag.REPEAT:
                repeat_stmt();
                break;
            case Tag.ID:
                Word w = (Word) tok;
                assign_stmt();
                break;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado token {IN, OUT, WHILE, REPEAT, IF, ID}, lido " + tok.toString() + " ('" + obtido + "')");
        }
        eat(Tag.SEMICOLON);
    }

    void assign_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        Node n1,n2;
        Word w = (Word) tok;
        eat(Tag.ID);
        if (tabela_simbolos.searchWord(w) == null) {
            throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". Variável " + w.toString() + " não declarada.");
        }
        n1 = new Node(tabela_simbolos.searchWord(w));
        eat(Tag.ASSIGN);
        n2 = simple_expr();
        if (n1.verifyAssignType(n2.type) == false) {
            throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". O tipo das expressões não são compatíveis para atribuição (" + n1.type + " e " + n2.type + ").");
        }
    }

    void if_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException {  //ok
        eat(Tag.IF);
        condition();
        eat(Tag.THEN);
        tabela_simbolos.addNewBlock();
        if (tok.tag == Tag.INT || tok.tag == Tag.FLOAT || tok.tag == Tag.CHAR) {
            decl_list();
        }
        stmt_list();
        if_sufix();
    }

    void if_sufix() throws SintaxException, LexicalException, SemanticException, SymbTableException {   //ok
        switch (tok.tag) {
            case Tag.END:
                eat(Tag.END);
                tabela_simbolos.removeLastBlock();
                break;
            case Tag.ELSE:
                eat(Tag.ELSE);
                if (tok.tag == Tag.INT || tok.tag == Tag.FLOAT || tok.tag == Tag.CHAR) {
                    decl_list();
                }
                stmt_list();
                eat(Tag.END);
                tabela_simbolos.removeLastBlock();
                break;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado token {END, ELSE}, lido " + tok.toString() + " ('" + obtido + "')");
        }
    }

    void condition() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        expression();
    }

    void repeat_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        eat(Tag.REPEAT);
        tabela_simbolos.addNewBlock();
        if (tok.tag == Tag.INT || tok.tag == Tag.FLOAT || tok.tag == Tag.CHAR) {
            decl_list();
        }
        stmt_list();
        stmt_suffix();
        tabela_simbolos.removeLastBlock();
    }

    void stmt_suffix() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        eat(Tag.UNTIL);
        condition();
    }

    void while_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        stmt_prefix();
        tabela_simbolos.addNewBlock();
        if (tok.tag == Tag.INT || tok.tag == Tag.FLOAT || tok.tag == Tag.CHAR) {
            decl_list();
        }
        stmt_list();
        eat(Tag.END);
        tabela_simbolos.removeLastBlock();
    }

    void stmt_prefix() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        eat(Tag.WHILE);
        condition();
        eat(Tag.DO);
    }

    void read_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        eat(Tag.IN);
        eat(Tag.OPENBRACKET);
        Word w = (Word) tok;
        eat(Tag.ID);
        if (tabela_simbolos.searchWord(w) == null) {
            throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". Variável " + w.toString() + " não declarada.");
        }
        eat(Tag.CLOSEBRACKET);
    }

    void write_stmt() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        eat(Tag.OUT);
        eat(Tag.OPENBRACKET);
        writable();
        eat(Tag.CLOSEBRACKET);
    }

    void writable() throws SintaxException, LexicalException, SemanticException, SymbTableException {  //ok
        switch (tok.tag) {
            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;
            default:
                if (tok.tag == Tag.ID || tok.tag == Tag.OPENBRACKET || tok.tag == Tag.NOT || tok.tag == Tag.SUB || tok.tag == Tag.FLOAT || tok.tag == Tag.CHAR || tok.tag == Tag.INT) {
                    simple_expr();
                } else {
                    String obtido = Tag.traduzir_tag(tok.tag);
                    throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado literal ou expressão simples, lido " + tok.toString() + " ('" + obtido + "')");
                }
        }
    }

    Node expression() throws SintaxException, LexicalException, SemanticException, SymbTableException {    //ok
        Node n1, n2;
        String type_resulting;
        n1 = simple_expr();
        if (tok.tag == Tag.EQ || tok.tag == Tag.NE || tok.tag == Tag.GT || tok.tag == Tag.GE || tok.tag == Tag.LT || tok.tag == Tag.LE) {
            relop();
            // não há restrição de tipos para comparação. Char poderia ser convertido para o código ASCII e realizar comparação
            // o tipo resultante de uma expressão de comparação é inteiro (não existe tipo booliano na linguagem, similiar a C. 0 é falso e diferente de verdadeiro)
            n2 = simple_expr();
            type_resulting = "inteiro";
            return new Node(type_resulting);
        }
        else{
            return n1;
        }
    }

    Node simple_expr() throws SintaxException, LexicalException, SemanticException, SymbTableException {    //ok
        Node n1, n2;
        String type_resulting;
        n1 = term();
        if (tok.tag == Tag.ADD || tok.tag == Tag.SUB || tok.tag == Tag.OR) {
            addop();
            n2 = simple_expr();
            type_resulting = n1.resultingArithmeticType(n2.type);
            if(n1.resultingArithmeticType(n2.type) == null){
                throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". O tipo das expressões não são compatíveis (" + n1.type + " e " + n2.type + ").");
            }
            return new Node(type_resulting);
        }
        else{
            return n1;
        }
    }

    Node term() throws SintaxException, LexicalException, SemanticException, SymbTableException {    //ok
        Node n1, n2;
        String type_resulting = null;
        n1 = factor_a();
        if (tok.tag == Tag.MUL || tok.tag == Tag.DIV || tok.tag == Tag.AND) {
            mulop();
            n2 = term();
            type_resulting = n1.resultingArithmeticType(n2.type);
            if(n1.resultingArithmeticType(n2.type) == null){
                throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". O tipo das expressões não são compatíveis (" + n1.type + " e " + n2.type + ").");
            }
            return new Node(type_resulting);
        }
        else{
            return n1;
        }
    }

    Node factor_a() throws SintaxException, LexicalException, SemanticException, SymbTableException {    //ok
        Node n;
        switch (tok.tag) {
            case Tag.NOT:
                eat(Tag.NOT);
                n = factor();
                return n;
            case Tag.SUB:
                eat(Tag.SUB);
                n = factor();
                return n;
            default:
                n = factor();
                return n;
        }
    }

    Node factor() throws SintaxException, LexicalException, SemanticException, SymbTableException {    //ok
        Node n;
        switch (tok.tag) {
            case Tag.ID:
                Word w = (Word) tok;
                eat(Tag.ID);
                if (tabela_simbolos.searchWord(w) == null) {
                    throw new SemanticException("Erro semântico na linha " + analisador_lexico.getLine() + ". Variável " + w.toString() + " não declarada.");
                }
                return new Node(tabela_simbolos.searchWord(w));
            case Tag.CONST_FLOAT:
                n = constant();
                return n;
            case Tag.CONST_INT:
                n = constant();
                return n;
            case Tag.CONST_CHAR:
                n = constant();
                return n;
            case Tag.OPENBRACKET:
                eat(Tag.OPENBRACKET);
                n = expression();
                eat(Tag.CLOSEBRACKET);
                return n;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado id, constante ou expressão entre (), lido " + tok.toString() + " ('" + obtido + "')");
        }
    }

    void relop() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        switch (tok.tag) {
            case Tag.EQ:
                eat(Tag.EQ);
                break;
            case Tag.GT:
                eat(Tag.GT);
                break;
            case Tag.GE:
                eat(Tag.GE);
                break;
            case Tag.LT:
                eat(Tag.LT);
                break;
            case Tag.LE:
                eat(Tag.LE);
                break;
            case Tag.NE:
                eat(Tag.NE);
                break;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado operadores {==, !=, >, >=, <, <=}, lido " + tok.toString() + " ('" + obtido + "')");
        }
    }

    void addop() throws SintaxException, LexicalException, SemanticException, SymbTableException { //ok
        switch (tok.tag) {
            case Tag.ADD:
                eat(Tag.ADD);
                break;
            case Tag.SUB:
                eat(Tag.SUB);
                break;
            case Tag.OR:
                eat(Tag.OR);
                break;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado operadores {+, -, ||}, lido " + tok.toString() + " ('" + obtido + "')");
        }
    }

    void mulop() throws SintaxException, LexicalException, SemanticException, SymbTableException {     //ok
        switch (tok.tag) {
            case Tag.MUL:
                eat(Tag.MUL);
                break;
            case Tag.DIV:
                eat(Tag.DIV);
                break;
            case Tag.AND:
                eat(Tag.AND);
                break;
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado operadores {*, /, &&}, lido " + tok.toString() + " ('" + obtido + "')");
        }
    }

    Node constant() throws SintaxException, LexicalException, SemanticException, SymbTableException {  //ok
        switch (tok.tag) {
            case Tag.CONST_INT:
                eat(Tag.CONST_INT);
                return new Node("inteiro");
            case Tag.CONST_FLOAT:
                eat(Tag.CONST_FLOAT);
                return new Node("real");
            case Tag.CONST_CHAR:
                eat(Tag.CONST_CHAR);
                return new Node("char");
            default:
                String obtido = Tag.traduzir_tag(tok.tag);
                throw new SintaxException("Erro sintático na linha " + analisador_lexico.getLine() + ". Esperado uma constante, lido " + tok.toString() + " ('" + obtido + "')");
        }
    }
}
