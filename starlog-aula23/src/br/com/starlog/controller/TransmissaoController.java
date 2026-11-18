package br.com.starlog.controller;

import br.com.starlog.model.CriptoMensagem;
import java.util.ArrayList;
import java.util.List;

/**
 * CAMADA CONTROLLER (O Maestro da Transmissão)
 *
 * Orquestra as requisições da View, convertendo os tipos brutos e alimentando o Model.
 *
 * REGRA DE ARQUITETURA:
 * O Controller manipula lógica e dados, NÃO componentes visuais (zero imports de javax.swing.*)!
 */
public class TransmissaoController {

    private final List<String> bufferTransmissoes = new ArrayList<>();

    /**
     * Orquestra a transmissão da mensagem.
     *
     * @param remetenteRaw Remetente vindo do campo de texto da tela
     * @param msgRaw       Mensagem vinda do campo de texto da tela
     * @param chaveStr     Chave digitada como texto na tela
     * @return Pacote formatado no padrão: [REMETENTE] #CHECKSUM -> TEXTO_CIFRADO
     */
    public String processarTransmissao(String remetenteRaw, String msgRaw, String chaveStr) {
        // 1. Converte a chave de texto para inteiro, com mensagem de erro limpa
        int chave;
        try {
            chave = Integer.parseInt(chaveStr.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Chave deve ser um número inteiro válido.");
        }

        // 2. Instancia o Model (a validação Fail-Fast acontece no construtor)
        CriptoMensagem cripto = new CriptoMensagem(remetenteRaw, msgRaw, chave);

        // 3. Obtém texto cifrado e checksum do Model
        String textoCifrado = cripto.cifrar();
        int checksum = cripto.calcularChecksum();

        // 4. Formata o pacote
        String pacote = String.format("[%s] #%d -> %s", cripto.getRemetente(), checksum, textoCifrado);

        // 5. Guarda no buffer
        bufferTransmissoes.add(pacote);

        // 6. Retorna o pacote formatado
        return pacote;
    }

    public int getTotalTransmissoes() {
        return bufferTransmissoes.size();
    }
}
