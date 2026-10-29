import jogo.*;
import jogador.*;
import java.io.*;


public class Velha
{
    private static final int COMPUTADOR_FACIL     = 1;
    private static final int COMPUTADOR_IMBATIVEL = 2;
    private static final int DOIS_JOGADORES       = 3;

    private static final String NOME_JOGADOR_1 = "Jogador 1";
    private static final String NOME_JOGADOR_2 = "Jogador 2";

    public static void main (String args [])
    {
        Tabuleiro           tabDoJogo;

        Jogador             jogador  = null; // de quem e a vez
        JogadorHumano       jogador1 = null; // sempre um humano
        Jogador             jogador2 = null; // computador (facil/imbativel) ou segundo humano

        AnalistaSituacional analista;

        int modo = 0;

        try
        {
            modo = escolherModo ();
        }
        catch (Exception e)
        {
            System.out.println("Erro: " + e.getMessage());
            return;
        }

        boolean jogador1Comeca = (int)(Math.random()*2) == 0;

        do
        {
            try
            {
                tabDoJogo = new Tabuleiro ();

                // quem comeca escolhe a marca; o outro fica com a oposta
                if (jogador1Comeca)
                {
                    jogador1 = new JogadorHumano (tabDoJogo, nomeDoJogador1(modo));
                    jogador2 = criarOponente (modo, tabDoJogo, marcaOposta(jogador1.getMarca()));
                }
                else
                {
                    jogador2 = criarOponente (modo, tabDoJogo, null);
                    jogador1 = new JogadorHumano (tabDoJogo, marcaOposta(jogador2.getMarca()), nomeDoJogador1(modo));
                }

                analista = new AnalistaSituacional (tabDoJogo);

                if (jogador1Comeca)
                    jogador=jogador1;
                else
                    jogador=jogador2;

                do
                {
                    jogador.facaSuaJogada();

                    if (jogador==jogador1)
                        jogador=jogador2;
                    else
                        jogador=jogador1;
                }
                while (!analista.fimDeJogo());

                System.out.println();
                System.out.println(tabDoJogo);

                anunciarResultado (modo, jogador1, analista.getMarcaDoVencedor());

                jogador1Comeca = !jogador1Comeca;
            }
            catch (Exception e)
            {
                System.out.println("Erro: " + e.getMessage());
                return;
            }
        }
        while (jogador1.querMaisUmaPartida());
    }

    // mostra o menu e devolve o modo escolhido (1, 2 ou 3)
    private static int escolherModo () throws IOException
    {
        System.out.println("===== JOGO DA VELHA =====");
        System.out.println("1 - Voce x Computador (facil)");
        System.out.println("2 - Voce x Computador (imbativel)");
        System.out.println("3 - Dois jogadores");
        System.out.println();

        while (true)
        {
            System.out.print("Escolha o modo de jogo (1 a 3): ");
            String resposta = Teclado.lerLinha();

            if (resposta == null)
                throw new IOException ("Entrada encerrada");

            try
            {
                int modo = Integer.parseInt(resposta.trim());

                if (modo >= COMPUTADOR_FACIL && modo <= DOIS_JOGADORES)
                    return modo;
            }
            catch (NumberFormatException erro)
            {} // nao era numero, vai perguntar de novo

            System.out.println("Opcao invalida, digite 1, 2 ou 3!");
        }
    }

    // no modo de dois jogadores os humanos tem nome; contra o computador, nao precisa
    private static String nomeDoJogador1 (int modo)
    {
        if (modo == DOIS_JOGADORES)
            return NOME_JOGADOR_1;

        return null;
    }

    // cria o adversario do jogador 1, conforme o modo.
    // marca == null significa "ele que escolhe/sorteia a marca"
    private static Jogador criarOponente (int modo, Tabuleiro tab, Marca marca) throws Exception
    {
        if (modo == DOIS_JOGADORES)
        {
            if (marca == null)
                return new JogadorHumano (tab, NOME_JOGADOR_2);

            return new JogadorHumano (tab, marca, NOME_JOGADOR_2);
        }

        if (modo == COMPUTADOR_IMBATIVEL)
        {
            if (marca == null)
                return new JogadorImbativel (tab);

            return new JogadorImbativel (tab, marca);
        }

        if (marca == null)
            return new JogadorAutomatico (tab);

        return new JogadorAutomatico (tab, marca);
    }

    private static Marca marcaOposta (Marca mrc) throws Exception
    {
        return new Marca (mrc.getSimbolo()=='X'?'O':'X');
    }

    private static void anunciarResultado (int modo, JogadorHumano jogador1, Marca vencedor)
    {
        if (vencedor == null)
            jogador1.deuVelha ();
        else
            if (modo == DOIS_JOGADORES)
                System.out.println("Parabens, " +
                                   (vencedor.equals(jogador1.getMarca()) ? NOME_JOGADOR_1 : NOME_JOGADOR_2) +
                                   "! Voce venceu!");
            else
                if (vencedor.equals(jogador1.getMarca()))
                    jogador1.vcGanhou ();
                else
                    jogador1.vcPerdeu ();
    }
}
