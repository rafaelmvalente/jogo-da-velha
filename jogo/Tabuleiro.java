package jogo;

public class Tabuleiro implements Cloneable
{
    // a principio, toda igual a null
    private Marca matriz [][] = new Marca [3][3];

    private boolean posicaoInvalida (int lin, int col)
    {
        return lin < 0 || lin > 2 || col < 0 || col > 2;
    }

    public boolean haMarcaNaPosicao (int lin, int col) throws Exception
    {
        if (this.posicaoInvalida(lin, col))
            throw new Exception ("Posicao invalida");

        return this.matriz[lin][col] != null;
    }

    public Marca getMarcaNaPosicao (int lin, int col) throws Exception
    {
        if (this.posicaoInvalida(lin, col))
            throw new Exception ("Posicao invalida");

        return this.matriz[lin][col];
    }

    public void setMarcaNaPosicao (Marca mrc, int lin, int col) throws Exception
    {
        if (this.posicaoInvalida(lin, col))
            throw new Exception ("Posicao invalida");

        if (mrc == null)
            throw new Exception ("Marca ausente");

        this.matriz[lin][col] = mrc;
    }

    @Override
    public String toString ()
    {
        String ret = "";

        for (int lin = 0; lin < 3; lin++)
        {
            for (int col = 0; col < 3; col++)
            {
                ret += " ";
                ret += (this.matriz[lin][col] == null ? " " : this.matriz[lin][col].toString());
                ret += " ";

                if (col < 2)
                    ret += "|";
            }

            ret += "\n";

            if (lin < 2)
                ret += "---+---+---\n";
        }

        return ret;
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

        Tabuleiro tab = (Tabuleiro) obj;

        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
            {
                Marca a = this.matriz[lin][col];
                Marca b = tab.matriz[lin][col];

                if (a == null)
                {
                    if (b != null)
                        return false;
                }
                else if (!a.equals(b))
                    return false;
            }

        return true;
    }

    @Override
    public int hashCode ()
    {
        int ret = 17;

        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
                ret = 7 * ret + (this.matriz[lin][col] == null ? 0 : this.matriz[lin][col].hashCode());

        if (ret < 0)
            ret = -ret;

        return ret;
    }

    // construtor padrao: tabuleiro vazio
    public Tabuleiro ()
    {}

    // construtor de copia
    public Tabuleiro (Tabuleiro modelo) throws Exception
    {
        if (modelo == null)
            throw new Exception ("Modelo ausente");

        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
                // Marca e imutavel (so tem getSimbolo), entao
                // compartilhar a referencia e seguro
                this.matriz[lin][col] = modelo.matriz[lin][col];
    }

    @Override
    public Object clone ()
    {
        Tabuleiro ret = null;

        try
        {
            ret = new Tabuleiro(this);
        }
        catch (Exception erro)
        {} // nao ha como dar erro, this nunca e null

        return ret;
    }
}
