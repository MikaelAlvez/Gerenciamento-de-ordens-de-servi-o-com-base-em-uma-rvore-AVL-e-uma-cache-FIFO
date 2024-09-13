package GerenciamentodeOSsTableHash;

public class No {
    int alt;
    No esq, dir;
    OrdensdeServicos os;

    public No(OrdensdeServicos data) {
        this.os = data;
        this.alt = 0;
        this.esq = null;
        this.dir = null;
    }
}
