# Jogo da Velha em Java

Jogo da velha para o terminal, feito em Java com orientação a objetos. Dá para jogar **contra o computador** (em dois níveis de dificuldade) ou **contra outra pessoa**.

## Modos de jogo

| Modo | Descrição |
|---|---|
| 1 – Você x Computador (fácil) | O computador segue regras simples: tenta ganhar, bloqueia o adversário, prepara uma jogada e, se nada servir, joga na primeira casa livre. Dá para vencer. |
| 2 – Você x Computador (imbatível) | O computador usa o algoritmo **minimax** e nunca perde. O melhor que você consegue é empatar. |
| 3 – Dois jogadores | Duas pessoas jogam no mesmo teclado, alternando as vezes. |

Quem começa é sorteado na primeira partida e alterna a cada nova partida. Quem começa escolhe a marca (X ou O).

## Como rodar

É preciso ter o **JDK** instalado (não só o Java de execução). Na pasta do projeto:

```bash
javac -encoding UTF-8 -d out jogo/*.java jogador/*.java Velha.java
java -cp out Velha
```

Para instalar o JDK no Ubuntu/Debian: `sudo apt install default-jdk`.

Durante a partida, a posição é escolhida digitando a **linha** e a **coluna** (de 1 a 3):

```
 X | O |   
---+---+---
   | X |   
---+---+---
 O |   |   
```

## Estrutura do projeto

```
├── Velha.java                    → menu e controle das partidas (main)
├── jogo/
│   ├── Marca.java                → a peça (X ou O), imutável
│   ├── Tabuleiro.java            → matriz 3x3 com as marcas
│   └── AnalistaSituacional.java  → diz se alguém ganhou ou se o jogo acabou
└── jogador/
    ├── Jogador.java              → interface: getMarca() e facaSuaJogada()
    ├── JogadorHumano.java        → joga pelo teclado
    ├── JogadorAutomatico.java    → computador "fácil" (regras simples)
    ├── JogadorImbativel.java     → computador imbatível (minimax)
    └── Teclado.java              → leitor de teclado compartilhado
```

### Como as classes se encaixam

`JogadorHumano`, `JogadorAutomatico` e `JogadorImbativel` implementam a interface `Jogador`. Por isso a classe `Velha` não precisa saber quem está jogando: ela só chama `jogador.facaSuaJogada()` (**polimorfismo**). Foi isso que tornou simples acrescentar o modo de dois jogadores e o computador imbatível: bastou criar novos `Jogador`.

## Como o computador imbatível funciona

O `JogadorImbativel` usa o algoritmo **minimax**:

1. Para cada casa livre, ele simula a jogada.
2. A partir dela, simula todas as respostas possíveis do adversário, depois as respostas dele, e assim por diante, até o fim da partida.
3. Dá uma nota a cada final: **positiva** se ele ganha (quanto mais rápido, melhor), **zero** se empata e **negativa** se perde.
4. Supõe que o adversário sempre escolhe a jogada pior para ele, e escolhe a jogada com a melhor nota.

Como o jogo da velha é pequeno (poucas centenas de milhares de partidas possíveis), ele analisa tudo em milissegundos. Entre jogadas de mesma nota, prefere o centro, depois os cantos, depois os lados.

Foi testado contra **todas** as sequências possíveis de jogadas do adversário (quase 25 mil partidas, jogando como X e como O, começando ou não): nenhuma derrota.

## Conceitos de Java usados

Interface, polimorfismo, encapsulamento, imutabilidade (`Marca`), pacotes, tratamento de exceções, `equals`/`hashCode`/`toString`, construtor de cópia e `clone`, matrizes e recursão (minimax).
