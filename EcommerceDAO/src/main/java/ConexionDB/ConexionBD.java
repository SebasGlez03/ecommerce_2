/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionDB;

import Interfaces.IConexionDB;
import Interfaces.IConexionDB;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *
 * @author SantiagoSanchez
 */
public class ConexionBD implements IConexionDB {

    private MongoClient mongoClient;
    private MongoDatabase database;

    // info de la base de datos
    private static final String USUARIO = "AGREGAR USUARIO";
    private static final String CONTRASENIA = "AGREGAR CONTRASENIA";
    private static final String IP_VPS = "AGREGAR SERVIDOR";
    private static final String PUERTO = "AGREGAR PUERTO";
    private static final String AUTH_BD = "admin";

    private final String CONEXION_STRING = String.format(
            "mongodb://%s:%s@%s:%s/?authSource=%s",
            USUARIO, CONTRASENIA, IP_VPS, PUERTO, AUTH_BD
    );
    private final String BD_REAL = "Ecommerce";
    private final String BD_TEST = "EcommerceTest";

    /**
     * Constructor de la conexion con la base de datos.
     *
     * @param esPrueba Si es TRUE se conecta a la base de datos de prueba, Si es
     * FALSE se conecta a la base de datos real.
     */
    public ConexionBD(boolean esPrueba) {
        if (esPrueba) {
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            ConnectionString connString = new ConnectionString(CONEXION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .codecRegistry(pojoCodecRegistry)
                    .build();

            mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase(BD_TEST);
        } else {
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            ConnectionString connString = new ConnectionString(CONEXION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .codecRegistry(pojoCodecRegistry)
                    .build();

            mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase(BD_REAL);
        }
    }

    /**
     * Obtiene la base de datos.
     *
     * @return Base de datos de mongoDB
     */
    @Override
    public MongoDatabase getDatabase() {
        return this.database;
    }

    /**
     * Cierra la conexion con la base de datos
     */
    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
