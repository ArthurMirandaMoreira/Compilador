public class Tag {
    public final static int

    // Tipos
    INT = 256,
            FLOAT = 257,
            CHAR = 258,
            ID = 259,
            LITERAL = 291,

            //constantes
            CONST_INT = 292,
            CONST_FLOAT = 293,
            CONST_CHAR = 294,

            // Palavras reservadas
            PROGRAM = 260,
            BEGIN = 261,
            END = 262,
            IF = 263,
            THEN = 264,
            ELSE = 265,
            REPEAT = 266,
            UNTIL = 267,
            WHILE = 268,
            DO = 269,
            IN = 270,
            OUT = 271,

            // Operadores relacionais
            ASSIGN = 272, // =
            EQ = 273, // ==
            NE = 274, // !=
            GE = 275, // >=
            LE = 276, // <=
            GT = 277, // >
            LT = 278, // <

            // Operadores lógicos
            AND = 279, // &&
            OR = 280, //  ||
            NOT = 281, // !

            // Operadores aritméticos
            ADD = 282, // +
            SUB = 283, // -
            MUL = 284, // *
            DIV = 285, // /

            // Pontuação
            SEMICOLON = 286, // ;
            COLON = 287, // :
            COMMA = 288, // ,
            OPENBRACKET = 289, // (
            CLOSEBRACKET = 290, // )

            // Fim de arquivo
            END_OF_FILE = 295;

    public static String traduzir_tag(int tag){
        String traducao = "";
        switch (tag) {
            case 256:
                traducao="INT";
                break;
            case 257:
                traducao="FLOAT";
                break;
            case 258:
                traducao="CHAR";
                break;
            case 259:
                traducao="ID";
                break;
            case 260:
                traducao="PROGRAM";
                break;
            case 261:
                traducao="BEGIN";
                break;
            case 262:
                traducao="END";
                break;
            case 263:
                traducao="IF";
                break;
            case 264:
                traducao="THEN";
                break;
            case 265:
                traducao="ELSE";
                break;
            case 266:
                traducao="REPEAT";
                break;
            case 267:
                traducao="UNTIL";
                break;
            case 268:
                traducao="WHILE";
                break;
            case 269:
                traducao="DO";
                break;
            case 270:
                traducao="IN";
                break;
            case 271:
                traducao="OUT";
                break;
            case 272:
                traducao="ASSIGN";
                break;
            case 273:
                traducao="==";
                break;
            case 274:
                traducao="!=";
                break;
            case 275:
                traducao=">=";
                break;
            case 276:
                traducao="<=";
                break;
            case 277:
                traducao=">";
                break;
            case 278:
                traducao="<";
                break;
            case 279:
                traducao="&&";
                break;
            case 280:
                traducao="||";
                break;
            case 281:
                traducao="!";
                break;
            case 282:
                traducao="+";
                break;
            case 283:
                traducao="-";
                break;
            case 284:
                traducao="*";
                break;
            case 285:
                traducao="/";
                break;
            case 286:
                traducao=";";
                break;
            case 287:
                traducao=":";
                break;
            case 288:
                traducao=",";
                break;
            case 289:
                traducao="(";
                break;
            case 290:
                traducao=")";
                break;
            case 291:
                traducao="LITERAL";
                break;
            case 292:
                traducao="CONST_INT";
                break;
            case 293:
                traducao="CONST_FLOAT";
                break;
            case 294:
                traducao="CONST_CHAR";
                break;
            case 295:
                traducao="END_OF_FILE";
                break;
        }
        return traducao;
    }

}
