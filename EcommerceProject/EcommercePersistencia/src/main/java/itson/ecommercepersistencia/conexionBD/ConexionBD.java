/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercepersistencia.conexionBD;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import itson.ecommercepersistencia.IConexionBD;
import java.io.InputStream;
import java.util.Properties;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author LENOVO
 */
public class ConexionBD implements IConexionBD{
    
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private final String CONEXION_STRING;
    private final String BD_REAL = "Ecommerce";
    private final String BD_TEST = "EcommerceTest";
    
    /**
     * Constructor de la conexion con la base de datos.
     *
     * @param esPrueba Si es TRUE se conecta a la base de datos de prueba, Si es
     *                 FALSE se conecta a la base de datos real.
     */
    public ConexionBD(boolean esPrueba) {

        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")){
            if (input != null) {
                props.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando config.properties", e);
        }

        // Variables de entorno
        String usuario     = System.getenv().getOrDefault("DB_USUARIO", props.getProperty("db.usuario"));
        String contrasenia = System.getenv().getOrDefault("DB_CONTRASENIA", props.getProperty("db.contrasenia"));
        String ip          = System.getenv().getOrDefault("DB_IP", props.getProperty("db.ip"));
        String puerto      = System.getenv().getOrDefault("DB_PUERTO", props.getProperty("db.puerto"));
        String authDb      = System.getenv().getOrDefault("DB_AUTH_DB", props.getProperty("db.auth_db"));

        if (usuario == null || contrasenia == null || ip == null || puerto == null || authDb == null) {
            throw new RuntimeException("Faltan credenciales de MongoDB");
        }

        this.CONEXION_STRING = String.format(
            "mongodb://%s:%s@%s:%s/?authSource=%s",
            usuario, contrasenia, ip, puerto, authDb
        );


        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        ConnectionString connString = new ConnectionString(CONEXION_STRING);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        this.mongoClient = MongoClients.create(settings);

        if (esPrueba) {
            this.database = mongoClient.getDatabase(BD_TEST);
        } else {
            this.database = mongoClient.getDatabase(BD_REAL);
        }
    }
    
    @Override
    public MongoDatabase getDatabase() {
        return this.database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public <T> MongoCollection<T> getCollection(String nombreColeccion, Class<T> claseEntidad) {
        return database.getCollection(nombreColeccion, claseEntidad);
    }
    
}
