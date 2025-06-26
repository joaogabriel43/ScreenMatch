package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    @Value("${omdb.api.key}")
    private String apiKey;

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = sc.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&apikey=" + apiKey);
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i<= dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + "&apikey=" + apiKey);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

                System.out.println("\n Top 5 episódios mais bem avaliados:");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .collect(Collectors.toList());


        episodios.forEach(System.out::println);

        System.out.println("Digite um treco do titulo do episódio que deseja buscar:");
        var tituloBusca = sc.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(tituloBusca.toLowerCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            Episodio episodio = episodioBuscado.get();
            System.out.println("Episódio encontrado: " + episodio.getTitulo() + ", Temporada: " + episodio.getTemporada() + ", Avaliação: " + episodio.getAvaliacao() + ", Data de Lançamento: " + episodio.getDataLancamento());
        } else {
            System.out.println("Nenhum episódio encontrado com o título: " + tituloBusca);
        }
//=====================================================================================================================================================================================================
        System.out.println("\n A partir de qual data você deseja filtrar os episódios? (formato: AAAA-MM-DD)");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento().isAfter(dataBusca))
                .sorted(Comparator.comparing(Episodio::getDataLancamento))
                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() + ", Episódio: " + e.getTitulo() + ", Data de Lançamento: " + e.getDataLancamento().format(formatter)));
//=====================================================================================================================================================================================================
        Map<Integer, Double> mediaPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println("\nMédia de avaliação por temporada:");
        mediaPorTemporada.forEach((temporada, media) -> {
            System.out.println("Temporada " + temporada + ": " + String.format("%.2f", media));
        });
//=====================================================================================================================================================================================================
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("\nEstatísticas de Avaliação:");
        System.out.println("Média: " + String.format("%.2f", est.getAverage()));
        System.out.println("Mínimo: " + est.getMin());
        System.out.println("Máximo: " + est.getMax());
        System.out.println("Total de Episódios: " + est.getCount());

    }
}
