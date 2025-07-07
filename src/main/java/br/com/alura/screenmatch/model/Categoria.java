package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO ("Action","Ação"),
    AVENTURA ("Adventure","Aventura"),
    COMEDIA ("Comedy","Comédia"),
    DRAMA ("Drama","Drama"),
    FANTASIA ("Fantasy","Fantasia"),
    FICCAO_CIENTIFICA ("Sci-Fi", "Ficção Científica"),
    TERROR ("Horror", "Terror"),
    SUSPENSE ("Thriller", "Suspense"),
    ANIMACAO ("Animation", "Animação"),
    DOCUMENTARIO ("Documentary", "Documentário"),
    MUSICAL ("Musical", "Musical"),
    ROMANCE ("Romance", "Romance"),
    MISTERIO ("Mystery", "Mistério"),
    CRIME ("Crime", "Crime"),;

    private String categoriaPortugues;
    private String categoriaOmdb;

    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaPortugues = categoriaPortugues;
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
