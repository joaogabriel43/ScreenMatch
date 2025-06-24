package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Value("${omdb.api.key}")
	private String apiKey;

	@Override
	public void run(String... args) throws Exception {
		var  consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?i=tt3896198&apikey=" + apiKey);
		System.out.println(json);
		//json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoApi.obterDados("https://www.omdbapi.com/?i=tt3896198&apikey=" + apiKey);
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);

	}
}
