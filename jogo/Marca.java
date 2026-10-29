package jogo;

public class Marca
{
    private char simbolo;

    public Marca (char smb) throws Exception
    {
        if (smb != 'X' && smb != 'O')
            throw new Exception ("Simbolo invalido");

        this.simbolo = smb;
    }

    public char getSimbolo ()
    {
        return this.simbolo;
    }

    @Override
    public String toString ()
    {
        return ""+simbolo;
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

        Marca mrc = (Marca) obj;

        return this.simbolo == mrc.simbolo;
    }

    @Override
    public int hashCode ()
    {
        int ret = 17;

        ret = 7 * ret + Character.valueOf(this.simbolo).hashCode();

        if (ret < 0)
            ret = -ret;

        return ret;
    }
}
