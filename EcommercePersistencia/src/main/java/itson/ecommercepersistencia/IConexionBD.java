/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author LENOVO
 */
public interface IConexionBD {
    public MongoDatabase getDatabase();
    public void close();
    // Agregamos este método para aprovechar la lógica de tu clase concreta
    public <T> MongoCollection<T> getCollection(String nombreColeccion, Class<T> claseEntidad);
}
