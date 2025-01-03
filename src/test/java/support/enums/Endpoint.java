package support.enums;

public enum Endpoint {

    FILME("/filme/{codigo}"),
    FILMES("/filmes"),
    FILMES_INVALID("/filmes-invalid"),
    SALVAR("/salvar"),
    PATCH("/api/usuario/{codigo}"),
    STATUS("/status"),
    FILME_EXTERNO("/idfilmeExterno/{codigo}"),
    VALIDAR_HEADER("/validarHeader");

    private final String path;

    Endpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
