package jogador;

import java.io.*;

// Leitor de teclado unico, compartilhado por todo o programa.
// Se cada jogador criasse o seu proprio BufferedReader em cima do System.in,
// um leitor poderia "roubar" texto que era do outro. Com um so, isso nao acontece.
public class Teclado
{
    private static final BufferedReader LEITOR =
        new BufferedReader (new InputStreamReader (System.in));

    private Teclado ()
    {} // so metodos estaticos, nao ha por que criar objetos

    // devolve a linha digitada, ou null se a entrada acabou
    public static String lerLinha () throws IOException
    {
        return LEITOR.readLine();
    }
}
