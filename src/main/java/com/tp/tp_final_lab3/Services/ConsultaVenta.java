package com.tp.tp_final_lab3.Services;

import com.tp.tp_final_lab3.Models.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConsultaVenta {

    public static void filtrarComboBox() {

    }

    /*public static void filtrarProducto(Producto producto, TableView tableProductos, ObservableList<Producto> productos) {

        String proveedor = producto.getProveedor();

        ArrayList<Producto> productosFiltrados = new ArrayList<>();

        for (Producto p : productos) {
            boolean proveedorCoincide = (proveedor == null) || proveedor.equals(p.getProveedor());

            if (proveedorCoincide) {
                productosFiltrados.add(p);
            }
        }
        tableProductos.setItems(FXCollections.observableArrayList(productosFiltrados));
    }*/
    public static void filtrarProducto(Producto producto, TableView tableProductos, ObservableList<Producto> productos) {
        String proveedor = producto.getProveedor();

        ArrayList<Producto> productosFiltrados = productos.stream()
                .filter(prov -> proveedor == null || proveedor.equals(prov.getProveedor()))
                .collect(Collectors.toCollection(ArrayList::new));

        ObservableList<Producto> productosFiltradosObservable = FXCollections.observableList(productosFiltrados);

        tableProductos.setItems(FXCollections.observableArrayList(productosFiltradosObservable));
    }
}
