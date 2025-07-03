package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaGemini;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")

public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    @Transient
    private List<Episodio> episodios =  new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dadosSerie) {


        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        if (dadosSerie.avaliacao() != null && !dadosSerie.avaliacao().equalsIgnoreCase("N/A")) {
            this.avaliacao = Double.valueOf(dadosSerie.avaliacao());
        } else {
            this.avaliacao = 0.0;
        }
        if (dadosSerie.genero() != null) {
            this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        } else {
            this.genero = null;
        }
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        try {
            this.sinopse = ConsultaGemini.obterTraducao(dadosSerie.sinopse().trim());
        } catch (Exception e) {
            this.sinopse = dadosSerie.sinopse();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return  "genero=" + genero +
                ",titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }
}
