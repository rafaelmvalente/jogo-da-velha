package jogador;

import jogo.Marca;
import jogo.Tabuleiro;

public class JogadorAutomatico implements Jogador, Cloneable
{
    // as 8 linhas possiveis de vitoria, na ordem: linhas, colunas, diagonal
    // principal e diagonal secundaria
    private static final int[][][] LINHAS =
    {
        {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}},
        {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}},
        {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}}
    };

    private Marca     marca     = null;
    private Tabuleiro tabDoJogo = null;

    public JogadorAutomatico (Tabuleiro tab) throws Exception
    {
        if (tab == null)
            throw new Exception ("Tabuleiro ausente");

        this.tabDoJogo = tab;

        // sorteia a marca
        if ((int)(Math.random()*2) == 0)
            this.marca = new Marca('X');
        else
            this.marca = new Marca('O');
    }

    public JogadorAutomatico (Tabuleiro tab, Marca mrc) throws Exception
    {
        if (tab == null)
            throw new Exception ("Tabuleiro ausente");

        if (mrc == null)
            throw new Exception ("Marca ausente");

        this.tabDoJogo = tab;
        this.marca     = mrc;
    }

    public Marca getMarca ()
    {
        return this.marca;
    }

    // Se alguma linha tem 2 marcas "alvo" e 1 posicao livre, joga na livre.
    // Com alvo = minha marca, isso e "tentar ganhar";
    // com alvo = marca do adversario, isso e "tentar atrapalhar".
    private boolean completarLinha (Marca alvo) throws Exception
    {
        for (int[][] linha : LINHAS)
        {
            int   qtdAlvo   = 0;
            int   qtdLivres = 0;
            int[] livre     = null;

            for (int[] pos : linha)
            {
                if (!this.tabDoJogo.haMarcaNaPosicao(pos[0], pos[1]))
                {
                    qtdLivres++;
                    livre = pos;
                }
                else
                if (this.tabDoJogo.getMarcaNaPosicao(pos[0], pos[1]).equals(alvo))
                    qtdAlvo++;
            }

            if (qtdAlvo == 2 && qtdLivres == 1)
            {
                this.tabDoJogo.setMarcaNaPosicao(this.marca, livre[0], livre[1]);
                return true;
            }
        }

        return false;
    }

    // Jogada de futuro: linha com 1 marca minha e 2 livres; planta outra marca.
    private boolean jogadaDeFuturo () throws Exception
    {
        for (int[][] linha : LINHAS)
        {
            int   qtdMinhas = 0;
            int   qtdLivres = 0;
            int[] livre     = null;

            for (int[] pos : linha)
            {
                if (!this.tabDoJogo.haMarcaNaPosicao(pos[0], pos[1]))
                {
                    qtdLivres++;
                    if (livre == null)
                        livre = pos;
                }
                else
                if (this.tabDoJogo.getMarcaNaPosicao(pos[0], pos[1]).equals(this.marca))
                    qtdMinhas++;
            }

            if (qtdMinhas == 1 && qtdLivres == 2)
            {
                this.tabDoJogo.setMarcaNaPosicao(this.marca, livre[0], livre[1]);
                return true;
            }
        }

        return false;
    }

    // joga na primeira posicao livre que encontrar
    private void jogarEmQualquerLugar () throws Exception
    {
        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
                if (!this.tabDoJogo.haMarcaNaPosicao(lin, col))
                {
                    this.tabDoJogo.setMarcaNaPosicao(this.marca, lin, col);
                    return;
                }
    }

    public void facaSuaJogada ()
    {
        try
        {
            Marca adversario = new Marca(this.marca.getSimbolo() == 'X' ? 'O' : 'X');

            // 1) tentando ganhar
            if (this.completarLinha(this.marca))
                return;

            // 2) tentando atrapalhar a vitoria do adversario
            if (this.completarLinha(adversario))
                return;

            // 3) jogada de futuro
            if (this.jogadaDeFuturo())
                return;

            // 4) qualquer lugar livre
            this.jogarEmQualquerLugar();
        }
        catch (Exception erro)
        {} // ignorando pois sei que nao estou usando posicao invalida nem comparando com equals um null
    }

    @Override
    public String toString ()
    {
        return "Jogador automatico (" + this.marca + ")";
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

        JogadorAutomatico jog = (JogadorAutomatico) obj;

        return this.marca.equals(jog.marca) &&
               this.tabDoJogo.equals(jog.tabDoJogo);
    }

    @Override
    public int hashCode ()
    {
        int ret = 17;

        ret = 7 * ret + this.marca.hashCode();
        ret = 7 * ret + this.tabDoJogo.hashCode();

        if (ret < 0)
            ret = -ret;

        return ret;
    }

    // construtor de copia
    public JogadorAutomatico (JogadorAutomatico modelo) throws Exception
    {
        if (modelo == null)
            throw new Exception ("Modelo ausente");

        this.marca     = modelo.marca;     // Marca e imutavel
        this.tabDoJogo = modelo.tabDoJogo; // mesmo tabuleiro: e o do jogo em andamento
    }

    @Override
    public Object clone ()
    {
        JogadorAutomatico ret = null;

        try
        {
            ret = new JogadorAutomatico(this);
        }
        catch (Exception erro)
        {} // nao ha como dar erro, this nunca e null

        return ret;
    }
}
