package itson.ecommercedominio.dtos;

import itson.ecommercedominio.Resenia;
import org.bson.types.ObjectId;
import java.util.Date;

public class ReseniaDTO {
    private ObjectId id;
//    private ObjectId idProducto; 
    private ObjectId idUsuario; 
    private String comentario;
    private int calificacion;
    private Date fecha;

    public ReseniaDTO() {}

    public ReseniaDTO(ObjectId id, ObjectId idUsuario, String comentario, int calificacion, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }

    public ReseniaDTO(ObjectId idUsuario, String comentario, int calificacion, Date fecha) {
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }
    
    public ReseniaDTO(Resenia resenia){
    
        this.id = resenia.getId();
        this.idUsuario = resenia.getIdUsuario();
        this.comentario = resenia.getComentario();
        this.calificacion = resenia.getCalificacion();
        this.fecha = resenia.getFecha();
        
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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


}