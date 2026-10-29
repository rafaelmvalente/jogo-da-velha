package jogador;

import jogo.Marca;
import jogo.Tabuleiro;
import java.io.*;

public class JogadorHumano implements Jogador, Cloneable
{
    private Marca     marca     = null;
    private Tabuleiro tabDoJogo = null;
    private String    nome      = null; // null quando joga sozinho contra o computador

    // pede a marca pelo teclado
    public JogadorHumano (Tabuleiro tab) throws Exception
    {
        this (tab, (String) null);
    }

    // pede a marca pelo teclado; nome aparece nas mensagens (usado no modo de dois jogadores)
    public JogadorHumano (Tabuleiro tab, String nome) throws Exception
    {
        if (tab == null)
            throw new Exception ("Tabuleiro ausente");

        this.tabDoJogo = tab;
        this.nome      = nome;

        // pede a marca ate o usuario digitar algo valido
        while (this.marca == null)
        {
            System.out.print((nome == null ? "Com" : nome + ", com") +
                             " qual marca quer jogar (X ou O)? ");
            String resposta = Teclado.lerLinha();

            if (resposta == null)
                throw new Exception ("Entrada encerrada");

            resposta = resposta.trim().toUpperCase();

            if (resposta.length() == 1)
                try
                {
                    this.marca = new Marca(resposta.charAt(0));
                }
                catch (Exception erro)
                {} // marca invalida, vai perguntar de novo

            if (this.marca == null)
                System.out.println("Marca invalida, digite X ou O!");
        }
    }

    public JogadorHumano (Tabuleiro tab, Marca mrc) throws Exception
    {
        this (tab, mrc, null);
    }

    public JogadorHumano (Tabuleiro tab, Marca mrc, String nome) throws Exception
    {
        if (tab == null)
            throw new Exception ("Tabuleiro ausente");

        if (mrc == null)
            throw new Exception ("Marca ausente");

        this.tabDoJogo = tab;
        this.marca     = mrc;
        this.nome      = nome;
    }

    public Marca getMarca ()
    {
        return this.marca;
    }

    public String getNome ()
    {
        return this.nome;
    }

    // le um inteiro de 1 a 3, repetindo a pergunta ate ser valido
    private int lerPosicao (String pergunta) throws IOException
    {
        while (true)
        {
            System.out.print(pergunta);
            String resposta = Teclado.lerLinha();

            if (resposta == null)
                throw new IOException ("Entrada encerrada");

            try
            {
                int valor = Integer.parseInt(resposta.trim());

                if (valor >= 1 && valor <= 3)
                    return valor - 1;
            }
            catch (NumberFormatException erro)
            {} // nao era numero, vai perguntar de novo

            System.out.println("Valor invalido, digite 1, 2 ou 3!");
        }
    }

    public void facaSuaJogada ()
    {
        try
        {
            System.out.println();

            if (this.nome != null)
                System.out.println("Vez de " + this.nome + " (" + this.marca + ")");

            System.out.println(this.tabDoJogo);

            while (true)
            {
                int lin = this.lerPosicao("Em qual linha quer jogar (1 a 3)? ");
                int col = this.lerPosicao("Em qual coluna quer jogar (1 a 3)? ");

                if (this.tabDoJogo.haMarcaNaPosicao(lin, col))
                    System.out.println("Essa posicao ja esta ocupada, escolha outra!");
                else
                {
                    this.tabDoJogo.setMarcaNaPosicao(this.marca, lin, col);
                    return;
                }
            }
        }
        catch (IOException erro)
        {
            // sem teclado nao ha como continuar a partida
            System.out.println();
            System.out.println("Entrada encerrada. Ate a proxima!");
            System.exit(0);
        }
        catch (Exception erro)
        {} // posicoes ja validadas acima
    }

    public void vcGanhou ()
    {
        System.out.println("Parabens, voce ganhou!");
    }

    public void vcPerdeu ()
    {
        System.out.println("Que pena, voce perdeu!");
    }

    public void deuVelha ()
    {
        System.out.println("Deu velha! O jogo empatou.");
    }

    public boolean querMaisUmaPartida ()
    {
        try
        {
            while (true)
            {
                System.out.print("Deseja jogar mais uma partida (S/N)? ");
                String resposta = Teclado.lerLinha();

                if (resposta == null)
                    return false;

                resposta = resposta.trim().toUpperCase();

                if (resposta.equals("S"))
                    return true;

                if (resposta.equals("N"))
                    return false;

                System.out.println("Resposta invalida, digite S ou N!");
            }
        }
        catch (IOException erro)
        {
            return false;
        }
    }

    @Override
    public String toString ()
    {
        return "Jogador humano (" + this.marca + ")";
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

        JogadorHumano jog = (JogadorHumano) obj;

        if (!this.marca.equals(jog.marca))
            return false;

        if (!this.tabDoJogo.equals(jog.tabDoJogo))
            return false;

        if (this.nome == null)
            return jog.nome == null;

        return this.nome.equals(jog.nome);
    }

    @Override
    public int hashCode ()
    {
        int ret = 17;

        ret = 7 * ret + this.marca.hashCode();
        ret = 7 * ret + this.tabDoJogo.hashCode();
        ret = 7 * ret + (this.nome == null ? 0 : this.nome.hashCode());

        if (ret < 0)
            ret = -ret;

        return ret;
    }

    // construtor de copia
    public JogadorHumano (JogadorHumano modelo) throws Exception
    {
        if (modelo == null)
            throw new Exception ("Modelo ausente");

        this.marca     = modelo.marca;     // Marca e imutavel
        this.tabDoJogo = modelo.tabDoJogo; // mesmo tabuleiro: e o do jogo em andamento
        this.nome      = modelo.nome;      // String tambem e imutavel
    }

    @Override
    public Object clone ()
    {
        JogadorHumano ret = null;

        try
        {
            ret = new JogadorHumano(this);
        }
        catch (Exception erro)
        {} // nao ha como dar erro, this nunca e null

        return ret;
    }
}
