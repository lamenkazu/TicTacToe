# Tic Tac Toe
Esse é um aplicativo Android onde é possível 
jogar contra outra pessoa ou contra um robô.

## Pros
- Não foi utilizada nenhuma biblioteca de terceiros para o funcionamento do sistema.
- Código desenvolvido com tecnologias atuais com Kotlin puro.
- Utilização de recursos essênciais do Android como Fragments, shapes para estilização própria, strings.xml inglês com tradução para português, colors.xml, Intents, Processos de background e Build Gradle.

## Contras
- O jogo tem apenas a opção de 3x3 padrão.
- A lista de matchs não está incrementado com um ranking de jogador por vitórias
- O robô não é inteligente, sendo apenas um nível "fácil" que insere na primeira casa disponível. (Incríveis chances de ainda perder :O)

## Fluxo de telas
### SplashArt - SplashActivity
Contém uma animação com o tabuleiro construído para o jogo, 
redireciona para o Menu do aplicativo.

### Tela Inicial - MenuActivity
Contém 3 botões
- vs Player -> Abre um Dialog para escolher os nomes dos jogadores e iniciar uma nova partida contra outro jogador.
- vs Bot -> Abre um Dialog para escolher o nome do jogador e iniciar uma nova partida contra um Bot.
- Game History -> Redireciona para a tela que lista o histórico das matchs.

### Histórico de jogos - GameHistoryActivity
Contém 2 Itens no toolbar
- (!) -> Mostra que você pode retomar um match ao clicar em um item da lista abaixo.
- (?) -> Mostra que você pode deletar um match ao pressionar o item da lista abaixo.
- Lista de Matchs -> Lista que contém os dados de partidas jogadas anteriormente ordenada de forma decrescente relativa à data de criação do match.
- Item Match -> Contém o nome do jogador 1 e jogador 2, caso seja um robô o jogador 2 se chama Robô.

### Tela do jogo - MainActivity
Contém alguns dos dados da partida, como nome dos jogadores, quantas partidas venceram e quantos empates ocorreram.
Além disso há uma exibição visual sobre quem é o jogador que deve jogar.
Ao finalizar o jogo exibe quem ganhou em um Toast, além de colorir de acordo com a cor do jogador a sequência vitoriosa.

