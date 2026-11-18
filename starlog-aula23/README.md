# StarLog-Zero • Operação Enigma: Caça ao Acoplamento (Aula 23)

Refatoração do módulo legado `TelaCaoticaLegada`, separando as responsabilidades no padrão **MVC** e validando o resultado com a suíte oficial **JUnit 5 (TDD)**.

## Estrutura

```
src/br/com/starlog/
├── amador/
│   └── TelaCaoticaLegada.java      # código legado (mantido como referência do "antes")
├── model/
│   └── CriptoMensagem.java         # Model: Fail-Fast, Cifra de César e checksum (sem Swing)
├── controller/
│   └── TransmissaoController.java  # Controller: conversão da chave, orquestração e formatação
└── test/
    └── CriptoMensagemTest.java     # suíte oficial com 8 testes JUnit 5
```

## Diagnóstico (Etapa 1)

O `ActionListener` do `btnDespacharCifrado` é um *code smell* (God Method / violação do SRP e do MVC): num único método misturam-se leitura da tela, validação, conversão de tipos, criptografia, cálculo de checksum, persistência em memória e exibição de mensagens (`JOptionPane`). Isso impede reutilizar a regra de negócio e testá-la sem abrir uma janela.

## O que foi implementado

| Etapa | Classe | Resumo |
|-------|--------|--------|
| 2 | `CriptoMensagem` | Barreira Fail-Fast (remetente, mensagem e chave 1..25), normalização com `trim().toUpperCase()`, `cifrar()` com wrap-around preservando espaços e pontuação, `calcularChecksum()` somando o ASCII |
| 3 | `TransmissaoController` | `Integer.parseInt` com captura de `NumberFormatException`/`NullPointerException` (mensagem limpa), instancia o Model e devolve `[REMETENTE] #CHECKSUM -> TEXTO_CIFRADO` |
| 4 | `CriptoMensagemTest` | 8 de 8 testes passando |

Model e Controller não importam `javax.swing.*`.

## Como executar os testes

Pela IDE: botão Play ao lado de `CriptoMensagemTest`.

Pelo terminal, com o `junit-platform-console-standalone` (ajuste o caminho do jar):

```bash
javac -encoding UTF-8 -cp junit-platform-console-standalone-1.10.2.jar -d out \
  src/br/com/starlog/amador/*.java src/br/com/starlog/model/*.java \
  src/br/com/starlog/controller/*.java src/br/com/starlog/test/*.java

java -jar junit-platform-console-standalone-1.10.2.jar execute -cp out \
  --select-class br.com.starlog.test.CriptoMensagemTest
```

Resultado esperado: `8 tests successful`, `0 tests failed`.
