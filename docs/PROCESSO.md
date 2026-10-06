# Documentação do processo — Trabalho 2 / MAF

## 1. Evolução do Trabalho 1

No Trabalho 1, a Nova Store possuía três telas principais: **Home**, **Detalhes do Produto** e **Pagamento**. A navegação era feita entre `Activities` utilizando `Intent`, e os produtos eram definidos de forma estática no layout.

No Trabalho 2, o projeto foi reorganizado para atender ao MAF. A aplicação passou a utilizar **Jetpack Compose**, uma `MainActivity` única, `NavHost`, `NavController`, `NavigationBar`, rotas nomeadas, listas dinâmicas e estado mantido em memória.

Fluxo anterior:

```text
Home → Produto → Pagamento
```

Fluxo atual:

```text
NavigationBar
├── Loja / Produtos → Detalhes do Produto → Carrinho → Pagamento
├── Cupons → Detalhes do Cupom → Carrinho
├── Carrinho → Pagamento
└── Sobre
```

## 2. Por que essas telas novas?

As telas foram escolhidas para manter o tema original de uma loja virtual e, ao mesmo tempo, transformar o protótipo do Trabalho 1 em um aplicativo funcional.

- **Produtos:** catálogo principal e primeira lista editável.
- **Detalhes do Produto:** exibe o item selecionado, controla quantidade e calcula o valor total.
- **Cupons:** segunda lista editável, separada dos produtos.
- **Detalhes do Cupom:** exibe o cupom selecionado, ativa/desativa e calcula a economia no carrinho.
- **Carrinho:** organiza os produtos escolhidos, quantidades e descontos.
- **Pagamento:** mantém a proposta do Trabalho 1 e fecha o fluxo da compra demonstrativa.
- **Sobre:** apresenta informações do projeto e completa uma área principal independente na navegação.

## 3. Decisões de configuração e organização

A principal decisão foi substituir a navegação distribuída entre várias `Activities` por uma estrutura centralizada com **Navigation Compose**.

As rotas ficam no `object Rotas`, evitando strings de navegação espalhadas pelo código. Os dados principais foram separados em `data class` (`Produto`, `Cupom` e `CarrinhoItem`). A `NovaStoreApp` concentra o estado em memória e repassa dados e ações para cada tela.

Essa organização foi escolhida porque:

- facilita adicionar novas telas;
- reduz duplicação de código de navegação;
- garante que o item tocado na lista seja o mesmo exibido em Detalhes;
- deixa claro onde ficam estado, modelos e rotas;
- atende diretamente aos requisitos de `NavHost`, `NavController` e rotas nomeadas.

## 4. Complexidade extra da tela de Detalhes

A tela **Detalhes do Produto** não apenas mostra os campos do item. Ela possui um seletor de quantidade entre 1 e 9, recalcula o total em tempo real e permite adicionar a quantidade escolhida ao carrinho.

Também existe uma navegação secundária a partir do próprio detalhe: ao adicionar o produto, o usuário é levado ao Carrinho.

Escolhemos essa funcionalidade porque é coerente com o contexto de uma loja e demonstra estado, cálculo e navegação além do exemplo básico de apenas exibir um item.

A tela **Detalhes do Cupom** também possui comportamento adicional: permite ativar/desativar o cupom, calcula a economia sobre o carrinho atual e aplica o desconto ao carrinho.

## 5. Dificuldade encontrada e solução

A maior dificuldade foi reorganizar a navegação e o compartilhamento de dados quando o projeto deixou de ter somente três telas.

No Trabalho 1, `Intent` era suficiente para abrir Produto e Pagamento. Com o crescimento do aplicativo, essa solução deixaria a navegação espalhada e mais difícil de manter.

A solução foi migrar para uma única Activity com Compose e utilizar `NavHost`/`NavController`. Os detalhes recebem o `id` pela rota e procuram o item correto na lista mantida no estado do aplicativo. Dessa forma, a lista e a tela de Detalhes utilizam a mesma fonte de dados.

## 6. Estado e persistência

Produtos, cupons e itens do carrinho são mantidos com `mutableStateListOf` dentro da execução do aplicativo.

Não foi adicionado Room, SQLite, Firebase ou outro banco. Isso foi intencional, pois o enunciado determina que persistência não é o foco desta etapa.

Ao encerrar o processo do aplicativo, itens adicionados durante a execução podem ser perdidos.

## 7. Checklist do enunciado

- [x] Novo repositório GitHub com README.
- [x] Pelo menos 7 telas navegáveis.
- [x] `NavHost` central com `NavController`.
- [x] `object Rotas` com rotas nomeadas.
- [x] `NavigationBar` funcional nas áreas principais.
- [x] Botões de voltar funcionais nas telas secundárias.
- [x] Pelo menos 2 `data class` de tipos diferentes (`Produto` e `Cupom`).
- [x] Duas telas de lista usando `LazyColumn + Card`.
- [x] Adicionar produto pela interface.
- [x] Adicionar cupom pela interface.
- [x] Remover produto pela interface.
- [x] Remover cupom pela interface.
- [x] Duas telas de Detalhes, uma para cada tipo de lista.
- [x] Item correto chega à tela de Detalhes através do ID da rota.
- [x] Tela de Produto possui funcionalidade extra: quantidade + total + navegação para Carrinho.
- [x] Campos de formulário funcionais.
- [x] Estado mantido apenas em memória.
- [x] Pagamento demonstrativo com Cartão/PIX.
- [x] CI para compilar o APK de debug.

## 8. Evidências visuais para a entrega

O enunciado solicita **prints reais da aplicação em execução ou um vídeo curto** acompanhando a documentação. Como esses registros precisam mostrar o app rodando em um emulador/dispositivo, eles devem ser capturados depois do teste final no Android Studio.

Sugestão de registros para anexar ao repositório em `docs/prints/`:

1. `01-produtos.png` — lista de Produtos e formulário.
2. `02-produto-adicionado.png` — novo produto aparecendo na lista.
3. `03-detalhe-produto.png` — Detalhes com quantidade e total.
4. `04-cupons.png` — lista e formulário de Cupons.
5. `05-detalhe-cupom.png` — Detalhes do Cupom e simulação.
6. `06-carrinho.png` — carrinho com quantidade e desconto.
7. `07-pagamento.png` — Cartão/PIX.
8. `08-sobre-bottom-navigation.png` — NavigationBar e tela Sobre.

Depois de adicionar os arquivos, eles podem ser referenciados nesta documentação com Markdown, por exemplo:

```md
![Tela de Produtos](prints/01-produtos.png)
```

## 9. Roteiro rápido para apresentação

1. Mostrar a tela Produtos e explicar que ela é a primeira lista.
2. Adicionar um produto e provar que o item aparece imediatamente.
3. Abrir o item recém-criado e mostrar que os dados corretos chegaram ao Detalhe.
4. Alterar a quantidade e enviar ao Carrinho.
5. Abrir Cupons, criar um cupom e mostrar a segunda lista.
6. Abrir Detalhes do Cupom, aplicar o desconto e mostrar o cálculo no Carrinho.
7. Navegar pelas quatro áreas da `NavigationBar`.
8. Finalizar a compra demonstrativa em Pagamento.
