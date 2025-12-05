/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package itson.ecommercedominio.enumeradores;

/**
 *
 * @author Beto_
 */
public enum Categoria {
    AUDIFONOS("Audífonos"),
    MONITORES("Monitores"),
    TECLADOS("Teclados"),
    RATONES("Ratones"),
    SILLAS("Sillas"),
    COMPONENTES("Componentes"),
    LAPTOPS("Laptops");

    private final String nombreVisible;

    Categoria(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
}
