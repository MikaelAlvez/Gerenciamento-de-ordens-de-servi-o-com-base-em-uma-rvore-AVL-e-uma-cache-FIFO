package GerenciamentodeOSs;

import java.util.ArrayList;
import java.util.List;

public class ArvoreAVL {
    private No raiz;
    private boolean rotacao;

    public ArvoreAVL() {
        this.raiz = null;
        this.rotacao = false;
    }  
    
    public void ordem() {
        this.ordem(raiz);
    }

    private void ordem(No raiz) {
        if (raiz != null) {
            this.ordem(raiz.esq);
            System.out.print(raiz.os.getCod() + " ");
            this.ordem(raiz.dir);
        }
    }
    
    public void adicionar(OrdensdeServicos servico) throws Exception {
    	raiz = adicionar(raiz, servico);
    }
    
    private No adicionar(No raiz, OrdensdeServicos servico) throws Exception {
        if (raiz == null)
            return new No(servico);

        if (servico.getCod() < raiz.os.getCod())
        	raiz.esq = adicionar(raiz.esq, servico);
        else if (servico.getCod() > raiz.os.getCod())
        	raiz.dir = adicionar(raiz.dir, servico);
        else
            throw new Exception("O nó com o código " + servico.getCod() + " já existe na árvore.");

        raiz.alt = novaAltura(raiz);

        return balancear(raiz);
    }
    
    public void remover(int cod) throws Exception {
    	raiz = remover(raiz, cod);
    }

    private No remover(No raiz, int cod) throws Exception {
        if (raiz == null) {
            throw new Exception("Árvore vazia ou nó não encontrado.");
        }

        if (cod < raiz.os.getCod())
        	raiz.esq = remover(raiz.esq, cod);
        else if (cod > raiz.os.getCod())
        	raiz.dir = remover(raiz.dir, cod);
        else {
            if (raiz.esq == null && raiz.dir == null) {
            	raiz = null;
            } else if (raiz.esq == null) {
                No temp = raiz;
                raiz = temp.dir;
                temp = null;
            } else if (raiz.dir == null) {
                No temp2 = raiz;
                raiz = temp2.esq;
                temp2 = null;
            } else {
                No temp3 = menor(raiz.dir);
                raiz.os = temp3.os;
                raiz.dir = remover(raiz.dir, temp3.os.getCod());
            }
        }

        if (raiz == null) {
            return raiz;
        }

        raiz.alt = novaAltura(raiz);

        return balancear(raiz);
    }
    
    private No menor(No raiz) {
        if (raiz == null)
            return null;

        No temp = raiz;

        while (temp.esq != null)
        	temp = temp.esq;

        return temp;
    }
    
    private No RDS(No raizAnt) {
        No novaRaiz = raizAnt.esq;
        No SubArvDir = novaRaiz.dir;

        novaRaiz.dir = raizAnt;
        raizAnt.esq = SubArvDir;

        raizAnt.dir = novaAltura(raizAnt);
        novaRaiz.dir = novaAltura(novaRaiz);

        this.rotacao = true;

        return novaRaiz;
    }

    private No RES(No raizAnt) {
        No novaRaiz = raizAnt.dir;
        No SubArvEsq = novaRaiz.esq;

        novaRaiz.esq = raizAnt;
        raizAnt.dir = SubArvEsq;

        raizAnt.dir = novaAltura(raizAnt);
        novaRaiz.dir = novaAltura(novaRaiz);

        this.rotacao = true;

        return novaRaiz;
    }
    
    private No balancear(No raiz) {
        int fbRaiz = getFB(raiz);
        int fbEsq = getFB(raiz.esq);
        int fbDir = getFB(raiz.dir);

        if (fbRaiz > 1) {
            if (fbEsq >= 0) {
                return RDS(raiz);
            } else {
            	raiz.esq = RES(raiz.esq);
                return RDS(raiz);
            }
        } else if (fbRaiz < -1) {
            if (fbDir <= 0) {
                return RES(raiz);
            } else {
            	raiz.dir = RDS(raiz.dir);
                return RES(raiz);
            }
        }

        return raiz;
    }
    
    private int altura(No raiz) {
        if (raiz == null)
            return -1;

        return raiz.alt;
    }
    
    public int getAltura() {
        return altura(raiz);
    }
    
    private int novaAltura(No raiz) {
        if (raiz == null) {
            return 0;
        }
        return 1 + Math.max(altura(raiz.esq), altura(raiz.dir));
    }
    
    public OrdensdeServicos buscar(int codigo) throws Exception {
        return buscar(raiz, codigo);
    }

    private No buscarNO(No raiz, int cod) {
        if (raiz == null) {
            return null;
        }
        if (cod < raiz.os.getCod()) {
            return buscarNO(raiz.esq, cod);
        } else if (cod > raiz.os.getCod()) {
            return buscarNO(raiz.dir, cod);
        } else {
            return raiz;
        }
    }

    private OrdensdeServicos buscar(No raiz, int cod) throws Exception {
        if (raiz == null) {
            throw new Exception("O nó com o código " + cod + " não existe na árvore.");
        }

        if (cod < raiz.os.getCod()) {
            return buscar(raiz.esq, cod);
        } else if (cod > raiz.os.getCod()) {
            return buscar(raiz.dir, cod);
        } else {
            return raiz.os;
        }
    }
    
    public List<OrdensdeServicos> listaOS() {
        List<OrdensdeServicos> lista = new ArrayList<>();
        listaOS(raiz, lista);
        return lista;
    }

    private void listaOS(No no, List<OrdensdeServicos> lista) {
        if (no != null) {
            listaOS(no.esq, lista);
            lista.add(no.os);
            listaOS(no.dir, lista);
        }
    }

    public void alterar(OrdensdeServicos os) throws Exception {
        No node = buscarNO(raiz, os.getCod());

        if (node != null) {
            node.os = os;
        } else {
            throw new Exception("O nó com o código " + os.getCod() + " não existe na árvore.");
        }
    }

    private int getFB(No raiz) {
        if (raiz == null)
            return 0;

        return altura(raiz.esq) - altura(raiz.dir);
    }
    
    public int getRegistros() {
        return getRegistros(raiz);
    }

    private int getRegistros(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + getRegistros(no.esq) + getRegistros(no.dir);
    }

    public boolean rotacionado() {
        boolean rotacionado = this.rotacao;
        this.rotacao = false;
        return rotacionado;
    }
}
