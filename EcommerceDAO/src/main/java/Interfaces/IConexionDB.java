package Interfaces;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */


import com.mongodb.client.MongoDatabase;

/**
 *
 * @author SantiagoSanchez
 */
public interface IConexionDB {

    public MongoDatabase getDatabase();

    public void close();
}
