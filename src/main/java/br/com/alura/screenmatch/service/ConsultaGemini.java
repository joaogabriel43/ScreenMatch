package br.com.alura.screenmatch.service;

import com.google.genai.client.GenerativeModel;
import com.google.genai.client.GenerationConfig;
import com.google.genai.client.VertexAI;
import com.google.genai.client.string.StringPart;
import com.google.common.collect.ImmutableList;

public class ConsultaGemini {

    public static String obterTraducao(String texto) {
        try {
            String apiKey = System.getenv("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                return "Chave de API do Gemini não encontrada na variável de ambiente GEMINI_API_KEY.";
            }

            VertexAI vertexAi = new VertexAI("gemini-1.0-pro", apiKey);
            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(2048)
                            .setTemperature(0.9F)
                            .setTopP(1.0F)
                            .build();
            GenerativeModel model =
                    new GenerativeModel(
                            "gemini-1.5-flash",
                            generationConfig,
                            vertexAi);

            String promptText = "traduza para o português o texto: " + texto;
            StringPart stringPart = new StringPart(promptText);

            String response = model.generateContent(ImmutableList.of(stringPart)).toString();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao chamar a API do Gemini: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
}