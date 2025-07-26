public class Node {
    public String type;

    public Node(String type) {
        this.type = type;
    }

    public boolean  verifyAssignType(String type2){
        String type1 = type;
        if (type1.equals("inteiro") && type2.equals("inteiro")) {
            return true;
        }
        if (type1.equals("real") && type2.equals("real")) {
            return true;
        }
        if (type1.equals("char") && type2.equals("char")) {
            return true;
        }
        return false;
    }

    public String resultingArithmeticType(String type2) {
        String type1 = type;
        String s = null;

        if (type1.equals("real")) {
            if (type2.equals("real") || type2.equals("inteiro")) {
                s = "real";
            } else {
                return null;
            }
        }
        if (type1.equals("inteiro")) {
            if (type2.equals("inteiro") || type2.equals("char")) {
                s = "inteiro";
            } else {
                s = "real";
            }
        }
        if (type1.equals("char")) {
            if (type2.equals("inteiro")) {
                s = "inteiro";
            } else {
                return null;
            }
        }
        return s;
    }
}
