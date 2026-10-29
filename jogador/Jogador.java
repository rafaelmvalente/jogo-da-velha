package jogador;

import jogo.Marca;
import jogo.Tabuleiro;

public interface Jogador
{
    // retorna a marca com a qual este jogador joga
    public Marca getMarca ();

    // faz uma jogada no tabuleiro do jogo
    public void facaSuaJogada ();
}
