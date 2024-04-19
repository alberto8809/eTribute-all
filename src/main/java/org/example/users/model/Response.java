package org.example.users.model;

public class Response {

    private String url_xml;

    private Descripcion descripcion;
    private String url_pdf;


    public Response() {
    }

    public String getUrl_xml() {
        return url_xml;
    }

    public void setUrl_xml(String url_xml) {
        this.url_xml = url_xml;
    }

    public Descripcion getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Descripcion descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl_pdf() {
        return url_pdf;
    }

    public void setUrl_pdf(String url_pdf) {
        this.url_pdf = url_pdf;
    }

    @Override
    public String toString() {
        return "Response{" +
                "url_xml='" + url_xml + '\'' +
                ", descripcion=" + descripcion +
                ", url_pdf='" + url_pdf + '\'' +
                '}';
    }
}
