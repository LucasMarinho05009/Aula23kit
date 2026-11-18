package br.com.starlog.model;

/**
 * CAMADA MODEL (Modelo de Domínio)
 * Representa uma mensagem criptográfica a ser transmitida.
 *
 * REGRA SAGRADA DE ARQUITETURA:
 * O Model NUNCA importa componentes visuais (zero imports de javax.swing.*).
 */
public class CriptoMensagem {
    private final String remetente;
    private final String mensagem;
    private final int chaveDeslocamento;

    public CriptoMensagem(String remetente, String mensagem, int chaveDeslocamento) {
        // Barreira Fail-Fast: valida tudo antes de qualquer atribuição
        if (remetente == null || remetente.trim().isEmpty()) {
            throw new IllegalArgumentException("Remetente não pode ser nulo ou vazio.");
        }
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem não pode ser nula ou vazia.");
        }
        if (chaveDeslocamento < 1 || chaveDeslocamento > 25) {
            throw new IllegalArgumentException("Chave deve estar entre 1 e 25.");
        }

        // Normalização
        this.remetente = remetente.trim().toUpperCase();
        this.mensagem = mensagem.trim().toUpperCase();
        this.chaveDeslocamento = chaveDeslocamento;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getChaveDeslocamento() {
        return chaveDeslocamento;
    }

    /**
     * Aplica o algoritmo da Cifra de César com deslocamento circular ('A'..'Z').
     * Preserva espaços e pontuações sem alteração.
     */
    public String cifrar() {
        StringBuilder cifrado = new StringBuilder();
        for (char c : this.mensagem.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                cifrado.append((char) ('A' + (c - 'A' + this.chaveDeslocamento) % 26));
            } else {
                cifrado.append(c);
            }
        }
        return cifrado.toString();
    }

    /**
     * Calcula o checksum somando o valor ordinal ASCII de cada caractere da mensagem em maiúsculo.
     */
    public int calcularChecksum() {
        int checksum = 0;
        for (char c : this.mensagem.toCharArray()) {
            checksum += (int) c;
        }
        return checksum;
    }
}
