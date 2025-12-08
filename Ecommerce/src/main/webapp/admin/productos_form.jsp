<%-- 
    Document   : productos_form
    Created on : 7 dic 2025, 23:50:02
    Author     : Beto_
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Producto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles_login.css">
</head>
<body>
    <div class="contenedor_login">
        <c:set var="esEdicion" value="${not empty producto}" />
        
        <form class="login_form" action="productos" method="POST">
            <h2>${esEdicion ? 'Editar Producto' : 'Nuevo Producto'}</h2>
            
            <input type="hidden" name="accion" value="${esEdicion ? 'editar' : 'crear'}">
            <c:if test="${esEdicion}">
                <input type="hidden" name="idProducto" value="${producto.id}">
            </c:if>

            <div class="form_group">
                <label>Nombre</label>
                <input type="text" name="nombre" value="${producto.nombre}" required>
            </div>
            
            <div class="form_group">
                <label>Descripción</label>
                <input type="text" name="descripcion" value="${producto.descripcion}" required>
            </div>
            
            <div class="form_group">
                <label>Precio</label>
                <input type="number" step="0.01" name="precio" value="${producto.precio}" required>
            </div>
            
            <div class="form_group">
                <label>Stock</label>
                <input type="number" name="stock" value="${producto.stock}" required>
            </div>
            
            <div class="form_group">
                <label>URL Imagen</label>
                <input type="text" name="imagenUrl" value="${producto.imagenUrl}">
            </div>
            
            <div class="form_group">
                <label>Categoría</label>
                <select name="categoria" style="width:100%; padding:10px;">
                    <option value="AUDIFONOS" ${producto.categoria == 'AUDIFONOS' ? 'selected' : ''}>Audífonos</option>
                    <option value="MONITORES" ${producto.categoria == 'MONITORES' ? 'selected' : ''}>Monitores</option>
                    <option value="TECLADOS" ${producto.categoria == 'TECLADOS' ? 'selected' : ''}>Teclados</option>
                    <option value="RATONES" ${producto.categoria == 'RATONES' ? 'selected' : ''}>Ratones</option>
                    <option value="SILLAS" ${producto.categoria == 'SILLAS' ? 'selected' : ''}>Sillas</option>
                    <option value="COMPONENTES" ${producto.categoria == 'COMPONENTES' ? 'selected' : ''}>Componentes</option>
                    <option value="LAPTOPS" ${producto.categoria == 'LAPTOPS' ? 'selected' : ''}>Laptops</option>
                </select>
            </div>

            <button type="submit" class="submit_btn">Guardar</button>
            
            <a href="?accion=listarAdmin" style="display:block; text-align:center; margin-top:10px;">Cancelar</a>
        </form>
    </div>
</body>
</html>