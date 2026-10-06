# Nova Store — Trabalho 2 (MAF)

Aplicativo Android acadêmico desenvolvido para a disciplina de Desenvolvimento de Aplicativos Móveis. O projeto evolui o Trabalho 1 (Home → Produto → Pagamento) para um **Mínimo Aplicativo Funcional**, com navegação centralizada, listas editáveis, telas de detalhes, carrinho e pagamento demonstrativo.

## Tecnologias

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose (`NavHost` + `NavController`)
- `mutableStateListOf` para estado em memória
- Android SDK 35 / minSdk 24
- Gradle executado com JDK 21 (o código Android permanece com compatibilidade Java 17)

## Telas

O app possui **7 telas funcionais**:

1. **Produtos** — lista em `LazyColumn + Card`, busca, adicionar e remover produtos.
2. **Detalhes do Produto** — recebe o produto correto pela rota, altera quantidade, calcula o total e adiciona ao carrinho.
3. **Cupons** — segunda lista em `LazyColumn + Card`, com adicionar e remover cupons.
4. **Detalhes do Cupom** — recebe o cupom correto, permite ativar/desativar, simula a economia e aplica o cupom ao carrinho.
5. **Carrinho** — altera quantidades, remove itens, aplica desconto e calcula subtotal/total.
6. **Pagamento** — escolha entre Cartão e PIX, formulário funcional e confirmação demonstrativa.
7. **Sobre** — resumo técnico e acadêmico do projeto.

A navegação principal usa **BottomNavigation/NavigationBar** entre Loja, Cupons, Carrinho e Sobre. As telas secundárias possuem botão de voltar funcional.

## Estrutura principal

```text
app/src/main/java/com/example/lojavisual/
├── MainActivity.kt      # Activity única e entrada do Compose
├── Models.kt            # Produto, Cupom e CarrinhoItem
├── Rotas.kt             # object Rotas com nomes das rotas
└── NovaStoreApp.kt      # NavHost, estado e telas
```

## Como rodar

1. Clone ou baixe este repositório.
2. Abra a pasta raiz no Android Studio.
3. Configure o **Gradle JDK como 21** caso o Android Studio não faça isso automaticamente.
4. Aguarde o Gradle Sync e instale o SDK 35 se solicitado.
5. Inicie um emulador Android ou conecte um dispositivo.
6. Clique em **Run ▶**.

Também existe um workflow de CI em `.github/workflows/android.yml` que executa `./gradlew assembleDebug` a cada push na `main`. O build do projeto foi validado com sucesso no GitHub Actions.

## Como testar o trabalho

1. Em **Produtos**, cadastre um produto novo e confirme que ele aparece imediatamente.
2. Abra um produto e confira se os dados exibidos são os do item tocado.
3. Altere a quantidade e adicione ao carrinho.
4. Remova um produto da lista.
5. Em **Cupons**, cadastre e remova cupons.
6. Abra um cupom, altere o status e aplique-o ao carrinho.
7. Navegue pelas quatro opções da barra inferior.
8. No Carrinho, altere quantidade/remova itens e confira os cálculos.
9. Vá ao Pagamento, teste Cartão e PIX e finalize a compra demonstrativa.
10. Verifique os botões de voltar nas telas secundárias.

## Persistência

Conforme o enunciado, **não há banco de dados**. Produtos, cupons e carrinho ficam somente em memória durante a execução do aplicativo e podem ser perdidos quando o processo é encerrado.

## Documentação do processo

A evolução do Trabalho 1, decisões técnicas, dificuldades, checklist do enunciado e o roteiro dos prints estão em [`docs/PROCESSO.md`](docs/PROCESSO.md).

> O pagamento é apenas visual/demonstrativo. Não existe cobrança real, backend ou integração financeira.
