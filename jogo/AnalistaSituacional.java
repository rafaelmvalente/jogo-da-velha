package jogo;

public class AnalistaSituacional
{
    private Tabuleiro tabDoJogo;

    public AnalistaSituacional (Tabuleiro tab) throws Exception
    {
        if (tab == null)
            throw new Exception ("Tabuleiro ausente");

        this.tabDoJogo = tab;
    }

    // devolve a marca se as 3 posicoes tiverem a mesma marca, ou null caso contrario
    private Marca trinca (int l1, int c1, int l2, int c2, int l3, int c3) throws Exception
    {
        Marca a = this.tabDoJogo.getMarcaNaPosicao(l1, c1);
        Marca b = this.tabDoJogo.getMarcaNaPosicao(l2, c2);
        Marca c = this.tabDoJogo.getMarcaNaPosicao(l3, c3);

        if (a != null && a.equals(b) && a.equals(c))
            return a;

        return null;
    }

    public Marca getMarcaDoVencedor ()
    {
        Marca ret = null;

        try
        {
            // linhas e colunas
            for (int i = 0; i < 3 && ret == null; i++)
            {
                ret = this.trinca(i, 0, i, 1, i, 2);

                if (ret == null)
                    ret = this.trinca(0, i, 1, i, 2, i);
            }

            // diagonais
            if (ret == null)
                ret = this.trinca(0, 0, 1, 1, 2, 2);

            if (ret == null)
                ret = this.trinca(0, 2, 1, 1, 2, 0);
        }
        catch (Exception erro)
        {} // nao ha como dar erro, as posicoes sao sempre validas

        return ret;
    }

    private boolean tabuleiroCheio ()
    {
        try
        {
            for (int lin = 0; lin < 3; lin++)
                for (int col = 0; col < 3; col++)
                    if (!this.tabDoJogo.haMarcaNaPosicao(lin, col))
                        return false;
        }
        catch (Exception erro)
        {} // nao ha como dar erro, as posicoes sao sempre validas

        return true;
    }

    public boolean fimDeJogo ()
    {
        if (this.getMarcaDoVencedor() != null)
            return true;

        if (this.tabuleiroCheio())
            return true;

        return false;
    }

    @Override
    public String toString ()
    {
        String ret = this.tabDoJogo.toString();

        Marca vencedor = this.getMarcaDoVencedor();

        if (vencedor != null)
            ret += "Vencedor: " + vencedor;
        else if (this.tabuleiroCheio())
            ret += "Empate";
        else
            ret += "Jogo em andamento";

        return ret + "\n";
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        AnalistaSituacional anl = (AnalistaSituacional) obj;

        return this.tabDoJogo.equals(anl.tabDoJogo);
    }

    @Override
    public int hashCode ()
    {
        int ret = 17; // qualquer positivo

        ret = 7 * ret + this.tabDoJogo.hashCode();

        if (ret < 0)
            ret = -ret;

        return ret;
    }
}
