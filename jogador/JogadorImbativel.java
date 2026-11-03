package jogador;

import jogo.Marca;
import jogo.Tabuleiro;

// Computador que nunca perde: usa o algoritmo minimax.
// Ele simula TODAS as partidas possiveis a partir do tabuleiro atual,
// supondo que o adversario sempre joga o melhor possivel, e escolhe a
// jogada que leva ao melhor resultado.
public class JogadorImbativel implements Jogador, Cloneable
{
    // as 8 linhas de vitoria: 3 linhas, 3 colunas e 2 diagonais
    private static final int[][][] LINHAS =
    {
        {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}},
        {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}},
        {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}}
    };

    // ordem em que as casas sao avaliadas: centro, cantos, depois os lados.
    // Quando duas jogadas empatam em nota, a de cima vence.
    private static final int[][] ORDEM =
    {
        {1,1},
        {0,0}, {0,2}, {2,0}, {2,2},
        {0,1}, {1,0}, {1,2}, {2,1}
    };

    private Marca     marca     = null;
    private Tabuleiro tabDoJogo = null;

    public JogadorImbativel (Tabuleiro tab) throws Exception
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

    public JogadorImbativel (Tabuleiro tab, Marca mrc) throws Exception
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

    private char meuSimbolo ()
    {
        return this.marca.getSimbolo();
    }

    private char simboloDoAdversario ()
    {
        return this.marca.getSimbolo() == 'X' ? 'O' : 'X';
    }

    // copia o tabuleiro do jogo para uma matriz simples (' ' = casa vazia),
    // onde o minimax pode testar jogadas e desfazer sem mexer no jogo de verdade
    private char[][] lerTabuleiro () throws Exception
    {
        char[][] ret = new char[3][3];

        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
                if (this.tabDoJogo.haMarcaNaPosicao(lin, col))
                    ret[lin][col] = this.tabDoJogo.getMarcaNaPosicao(lin, col).getSimbolo();
                else
                    ret[lin][col] = ' ';

        return ret;
    }

    // devolve 'X' ou 'O' se alguem fez uma trinca, ou ' ' se ninguem
    private char vencedor (char[][] t)
    {
        for (int[][] linha : LINHAS)
        {
            char a = t[linha[0][0]][linha[0][1]];
            char b = t[linha[1][0]][linha[1][1]];
            char c = t[linha[2][0]][linha[2][1]];

            if (a != ' ' && a == b && b == c)
                return a;
        }

        return ' ';
    }

    private boolean cheio (char[][] t)
    {
        for (int lin = 0; lin < 3; lin++)
            for (int col = 0; col < 3; col++)
                if (t[lin][col] == ' ')
                    return false;

        return true;
    }

    // Da uma nota ao tabuleiro, do ponto de vista deste jogador:
    //   positiva = eu ganho (quanto mais rapido, maior a nota)
    //   zero     = empate
    //   negativa = eu perco (quanto mais tarde, menos ruim)
    // minhaVez diz de quem e a vez de jogar neste tabuleiro simulado.
    private int minimax (char[][] t, boolean minhaVez, int profundidade)
    {
        char v = this.vencedor(t);

        if (v == this.meuSimbolo())       return 10 - profundidade;
        if (v == this.simboloDoAdversario()) return profundidade - 10;
        if (this.cheio(t))                return 0;

        // na minha vez escolho a MAIOR nota; na do adversario, ele escolhe a MENOR
        int melhor = minhaVez ? -100 : 100;

        for (int[] pos : ORDEM)
            if (t[pos[0]][pos[1]] == ' ')
            {
                t[pos[0]][pos[1]] = minhaVez ? this.meuSimbolo() : this.simboloDoAdversario();
                int nota = this.minimax(t, !minhaVez, profundidade + 1);
                t[pos[0]][pos[1]] = ' '; // desfaz a jogada simulada

                if (minhaVez)
                    melhor = Math.max(melhor, nota);
                else
                    melhor = Math.min(melhor, nota);
            }

        return melhor;
    }

    // 0 = centro, 1 = cantos, 2 = lados
    private int categoria (int[] pos)
    {
        if (pos[0] == 1 && pos[1] == 1)
            return 0;

        if ((pos[0] + pos[1]) % 2 == 0)
            return 1;

        return 2;
    }

    public void facaSuaJogada ()
    {
        try
        {
            char[][] t = this.lerTabuleiro();

            int     melhorNota = -100;
            int[][] melhores   = new int[9][];
            int     qtd        = 0;

            // testa cada casa vazia e guarda as de maior nota
            for (int[] pos : ORDEM)
                if (t[pos[0]][pos[1]] == ' ')
                {
                    t[pos[0]][pos[1]] = this.meuSimbolo();
                    int nota = this.minimax(t, false, 1);
                    t[pos[0]][pos[1]] = ' ';

                    if (nota > melhorNota)
                    {
                        melhorNota = nota;
                        qtd        = 0;
                    }

                    if (nota == melhorNota)
                    {
                        melhores[qtd] = pos;
                        qtd++;
                    }
                }

            if (qtd == 0)
                return; // tabuleiro cheio, nao ha o que jogar

            // entre as melhores, so as da mesma categoria da primeira (centro > canto > lado);
            // sorteia entre elas pra partidas nao ficarem todas iguais
            int categoria = this.categoria(melhores[0]);
            int n         = 0;

            while (n < qtd && this.categoria(melhores[n]) == categoria)
                n++;

            int[] escolhida = melhores[(int)(Math.random()*n)];

            this.tabDoJogo.setMarcaNaPosicao(this.marca, escolhida[0], escolhida[1]);
        }
        catch (Exception erro)
        {} // nao ha como dar erro: so uso posicoes validas e marca nao nula
    }

    @Override
    public String toString ()
    {
        return "Jogador imbativel (" + this.marca + ")";
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

        JogadorImbativel jog = (JogadorImbativel) obj;

        return this.marca.equals(jog.marca) &&
               this.tabDoJogo.equals(jog.tabDoJogo);
    }

    @Override
    public int hashCode ()
    {
        int ret = 17; // qualquer positivo

        ret = 7 * ret + this.marca.hashCode();
        ret = 7 * ret + this.tabDoJogo.hashCode();

        if (ret < 0)
            ret = -ret;

        return ret;
    }

    // construtor de copia
    public JogadorImbativel (JogadorImbativel modelo) throws Exception
    {
        if (modelo == null)
            throw new Exception ("Modelo ausente");

        this.marca     = modelo.marca;     // Marca e imutavel
        this.tabDoJogo = modelo.tabDoJogo; // mesmo tabuleiro: e o do jogo em andamento
    }

    @Override
    public Object clone ()
    {
        JogadorImbativel ret = null;

        try
        {
            ret = new JogadorImbativel(this);
        }
        catch (Exception erro)
        {} // nao ha como dar erro, this nunca e null

        return ret;
    }
}
