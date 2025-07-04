package br.com.alura.screenmatch.service;

public class GeminiApiTest {
    public static void main(String[] args) {
        System.out.println("Iniciando teste de tradução com a API do Gemini...");
        String textoParaTraduzir = "Hello, this is a test to check the API connection.";

        String resultado = ConsultaGemini.obterTraducao(textoParaTraduzir);

        System.out.println("\n--- RESULTADO DO TESTE ---");
        System.out.println(resultado);
        System.out.println("--------------------------\n");

        if (resultado.startsWith("Erro")) {
            System.out.println("O teste falhou. Verifique a mensagem de erro acima.");
            System.out.println("Causas comuns: Chave de API incorreta, variável de ambiente não carregada (reinicie a IDE), ou problemas de faturamento na conta do Google Cloud.");
        } else {
            System.out.println("O teste foi concluído. Se você viu um texto traduzido acima, a conexão está funcionando.");
        }
    }
}

