package com.example.projeto.modelo;

public class Request {

    private int id, ano;

    private String titulo, serie, autor, capa;

    /*public Request(int id, int customer_id, String title, String description, ENUM('low', 'medium', 'high') priority,
    ENUM('new', 'in_progress', 'completed', 'canceled') status, int current_technician_id, DATETIME canceled_at, int canceled_by,
    DATETIME created_at, DATETIME updated_at){
        this.id = id;
        this.customer_id = customer_id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.current_technician_id = current_technician_id;
        this.canceled_at = canceled_at;
        this.canceled_by = canceled_by;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    //respetivos getters e setters
    */

    public Request(int id, String capa, int ano, String titulo, String serie, String autor) {
        this.id =id;
        this.ano = ano;
        this.capa = capa;
        this.titulo = titulo;
        this.serie = serie;
        this.autor = autor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
