package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    @Autowired
    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar serie por titulo
                    5 - Buscar série por ator
                    6 - Buscar Top 5 series
                    7 - Buscar série por categoria
                    8 - Filtrar séries por temporada e avaliação

                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePortAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriePorTemporadaEAvaliacao();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Série já consta no banco de dados.");
        } else {
            var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&apikey=" + apiKey);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            Serie serie = new Serie(dados);
            repositorio.save(serie);
            System.out.println("Série adicionada ao banco de dados:");
            System.out.println(serie);
        }
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome:");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + "&apikey=" + apiKey);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscarSeriePorTitulo() {
        System.out.println("Digite o título da série:");
        String titulo = sc.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(titulo);

        if (serie.isPresent()) {
            System.out.println("Série encontrada: " + serie.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }
    private void buscarSeriePortAtor() {
        System.out.println("Digite o nome do ator:");
        String nomeAtor = sc.nextLine();
        System.out.println("Digite a avaliação mínima:");
        Double avaliacaoMinima = sc.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoMinima);

        if (seriesEncontradas.isEmpty()) {
            System.out.println("Nenhuma série encontrada com o ator: " + nomeAtor);
        } else {
            System.out.println("Séries encontradas com o ator " + nomeAtor + ":");
            seriesEncontradas.forEach(s ->
                    System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
        }
    }
    private void buscarTop5Series() {
        List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();

        if (top5Series.isEmpty()) {
            System.out.println("Nenhuma série encontrada.");
        } else {
            System.out.println("Top 5 Séries:");
            top5Series.forEach(s -> System.out.println(s.getTitulo() + " - Avaliação: " + s.getAvaliacao()));
        }
    }
    private void buscarSeriePorCategoria() {
        System.out.println("Digite a categoria da série (ex: DRAMA, COMÉDIA):");
        var nomeGenero = sc.nextLine();
        try {
            Categoria categoria = Categoria.fromPortugues(nomeGenero);
            List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);

            if (seriesPorCategoria.isEmpty()) {
                System.out.println("Nenhuma série encontrada na categoria: " + nomeGenero);
            } else {
                System.out.println("Séries encontradas com o genero: " + nomeGenero + ":");
                seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo()));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Categoria inválida. Por favor, digite uma categoria válida (ex: Ação, Comédia, Drama).");
        }
    }
    private void buscarSeriePorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries com até quantas temporadas? ");
        var totalTemporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = sc.nextDouble();
        sc.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("**** Séries filtradas ****");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + " (temporadas: " + s.getTotalTemporadas() + ", avaliação: " + s.getAvaliacao() + ")"));
    }
}
