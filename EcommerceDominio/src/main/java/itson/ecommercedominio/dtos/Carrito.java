/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercedominio.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class Carrito {
    private Map<ObjectId, ItemCarrito> items;

    public Carrito(Map<ObjectId, ItemCarrito> items) {
        this.items = items;
    }
    
    public void agregarItem(ItemCarrito itemNuevo) {
        if (items.containsKey(itemNuevo.getProductoId())) {
            // Si ya existe, solo sumamos la cantidad
            ItemCarrito itemExistente = items.get(itemNuevo.getProductoId());
            itemExistente.setCantidad(itemExistente.getCantidad() + itemNuevo.getCantidad());
        } else {
            // Si no existe, lo agregamos al mapa
            items.put(itemNuevo.getProductoId(), itemNuevo);
        }
    }

    public void eliminarItem(String idProductoHex) {
        if(idProductoHex != null) {
            items.remove(new ObjectId(idProductoHex));
        }
    }

    public void vaciar() {
        items.clear();
    }

    public Double getTotal() {
        double total = 0.0;
        for (ItemCarrito item : items.values()) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public int getCantidadItems() {
        return items.values().stream().mapToInt(ItemCarrito::getCantidad).sum();
    }
    
    public List<ItemCarrito> getListaItems() {
        return new ArrayList<>(items.values());
    }
}
