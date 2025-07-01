package br.com.alura.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {

    public static String obterTraducao(String texto) {
        try {

            Client client = new Client();

            String modelToUse = "gemini-1.5-flash";
            String promptText = "traduza para o português o texto: " + texto;

            GenerateContentResponse response = client.models.generateContent(modelToUse, promptText, null);
            return response.text();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao chamar a API do Gemini: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
}