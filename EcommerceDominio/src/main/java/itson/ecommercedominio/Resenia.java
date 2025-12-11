package itson.ecommercedominio;

import org.bson.types.ObjectId;
import java.util.Date;

public class Resenia {

    private ObjectId id;
    private ObjectId idProducto;
    private ObjectId idUsuario;
    private String comentario;
    private int calificacion;
    private Date fecha;
    private String nombreUsuario;

    public Resenia() {
    }

    public Resenia(ObjectId idProducto, ObjectId idUsuario, String comentario, int calificacion, Date fecha, String nombreUsuario) {
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
        this.nombreUsuario = nombreUsuario;
    }

    public Resenia(ObjectId id, ObjectId idProducto, ObjectId idUsuario, String comentario, int calificacion, Date fecha) {
        this.id = id;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(ObjectId idProducto) {
        this.idProducto = idProducto;
    }

    public ObjectId getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

}
