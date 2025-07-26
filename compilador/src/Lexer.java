
import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file; // arquivo fonte
    private HashMap<String, Word> words = new HashMap<>();

    // Método para inserir palavras reservadas
    private void reserve(Word w) {
        words.put(w.toString(), w); //
    }

    private Word getWord(Word w) {
        Word word = (Word) words.get(w.toString());
        if (word != null) {
            return w;       // palavra já cadastrada
        }
        else return null;   // palavra não cadastrada ainda
    }

    // Método para devolver a tabela de símbolos
    public HashMap<String, Word> getTable() {
        return words;
    }

    public int getLine() {
        return line;
    }

    // Método construtor
    public Lexer(String fileName) throws FileNotFoundException {
        file = new FileReader(fileName);
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }

        // Insere palavras reservadas na HashTable
        reserve(new Word("program", Tag.PROGRAM));
        reserve(new Word("begin", Tag.BEGIN));
        reserve(new Word("end", Tag.END));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("repeat", Tag.REPEAT));
        reserve(new Word("until", Tag.UNTIL));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("in", Tag.IN));
        reserve(new Word("out", Tag.OUT));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("char", Tag.CHAR));
    }

    // Lê o próximo caractere do arquivo
    private void readch() throws IOException {
        ch = (char) file.read();
    }

    // Lê o próximo caractere do arquivo e verifica se é igual a c
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    public Token nextToken() throws IOException, LexicalException {
        // Desconsidera delimitadores e comentários
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                line++; // conta linhas
            } // desconsidera comentários:
            // comentário de uma linha
            else if (ch == '%') {
                while (true) {
                    readch();
                    if (ch == '\n') {
                        line++;
                        break;
                    } else {
                        continue;
                    }
                }
            } // comentário de várias linhas 
            else if (ch == '{') {
                while (true) {
                    readch(ch);
                    if (ch == '\n') {
                        line++;
                    } else if (ch == (char) -1) {
                        throw new LexicalException("Comentário não fechado, na linha " + line);
                    } else if (ch == '}') {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        switch (ch) {
            // Operadores e pontuação
            case '&':
                if (readch('&')) {
                    return Word.and;
                } else {
                    // esperado outro '&', a linguagem não suporta apenas um &
                    throw new LexicalException("Token mal formado: Esperado '&', lido " + ch + ", na linha " + line);
                }
            case '|':
                if (readch('|')) {
                    return Word.or;
                } else {
                    // esperado outro '|', a linguagem não suporta apenas um |
                    throw new LexicalException("Token mal formado: Esperado '|', lido " + ch + ", na linha " + line);
                }
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return new Token(Tag.ASSIGN);
                }
            case '<':
                if (readch('=')) {
                    return Word.le;
                } else {
                    return new Token(Tag.LT);
                }
            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return new Token(Tag.GT);
                }
            case '!':
                if (readch('=')) {
                    return new Token(Tag.NE);
                } else {
                    return new Token(Tag.NOT);
                }
            case '+':
                readch();
                return new Token(Tag.ADD);
            case '-':
                readch();
                return new Token(Tag.SUB);
            case '*':
                readch();
                return new Token(Tag.MUL);
            case '/':
                readch();
                return new Token(Tag.DIV);
            case ':':
                readch();
                return new Token(Tag.COLON);
            case ';':
                readch();
                return new Token(Tag.SEMICOLON);
            case ',':
                readch();
                return new Token(Tag.COMMA);
            case '(':
                readch();
                return new Token(Tag.OPENBRACKET);
            case ')':
                readch();
                return new Token(Tag.CLOSEBRACKET);

        }
        // Números
        if (Character.isDigit(ch)) {
            int parte_inteira = 0;
            float parte_decimal = 0;
            do {
                parte_inteira = 10 * parte_inteira + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            // caso seja float
            if (ch == '.') {
                readch();
                if (Character.isDigit(ch)) {
                    int potencia = -1;
                    do {
                        parte_decimal += Character.digit(ch, 10) * Math.pow(10, potencia);
                        potencia--;
                        readch();
                    } while (Character.isDigit(ch));
                    if (ch != '.') {
                        return new Num(parte_inteira + parte_decimal);
                    } else {
                        throw new LexicalException("Float mal formado (não pode haver mais de um '.'), na linha " + line);
                    }
                } else {
                    throw new LexicalException("Float mal formado (esperado dígito, encontrado " + ch + "), na linha " + line);
                }
            } else {
                return new Num(parte_inteira);
            }
        }

        // Chars
        if (ch == '\'') {
            StringBuffer sb = new StringBuffer();
            sb.append(ch);
            readch();
            sb.append(ch);
            readch();
            if (ch == '\'') {
                sb.append(ch);
                readch();
                String s = sb.toString();
                return new Word(s, Tag.CONST_CHAR);
            }
            throw new LexicalException("Char inválido (esperado: ', encontrado: " + ch + "), na linha " + line);
        }

        // Literais
        if (ch == '"') {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
                if (ch == '\n') {
                    throw new LexicalException("Quebra de linha dentro de um literal, na linha " + line);
                }
                if (ch == (char) -1) {
                    throw new LexicalException("Fim de arquivo inesperado, na linha " + line);
                }
            } while (ch != '"');
            sb.append('"');
            String s = sb.toString();
            readch();
            return new Word(s, Tag.LITERAL);
        }

        // Identificadores e palavras reservadas
        if (Character.isLetter(ch) || ch == '_') {
            StringBuffer sb = new StringBuffer();
            do {
                ch = Character.toLowerCase(ch);
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || ch == '_');
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null) {
                return w; // palavra reservada
            }
            w = new Word(s, Tag.ID);    // identificador
            return w;
        }

        // Fim de arquivo
        if (ch == (char) -1) {
            return new Token(Tag.END_OF_FILE);
        }
        throw new LexicalException("Caractere inválido na linha " + line);
    }
}
