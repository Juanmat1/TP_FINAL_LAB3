package com.tp.tp_final_lab3.controllers;

import com.tp.tp_final_lab3.Models.Usuario;
import com.tp.tp_final_lab3.Repository.Jackson;
import com.tp.tp_final_lab3.Services.ControllersMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class crudUsuariosController implements Initializable, IAdminCrud<Usuario>{

    private final String pathJson = "src/main/java/com/tp/tp_final_lab3/Archives/usuarios.json";
    private ObservableList<Usuario> observableList = FXCollections.observableArrayList(Jackson.deserializarArrayList(pathJson,Usuario.class));
    @FXML
    private TableView<Usuario> tableUsuario;

    @FXML
    private TableColumn<Usuario, Integer> idColumn;

    @FXML
    private TableColumn<Usuario, String> nombreColumn;

    @FXML
    private TableColumn<Usuario, String> apellidoColumn;

    @FXML
    private TableColumn<Usuario, String> dniColumn;
    @FXML
    private TableColumn<Usuario, String> usuarioColumn;

    @FXML
    private TableColumn<Usuario, LocalDate> fechaCreacionColumn;

    @FXML
    private TableColumn<Usuario, Usuario.Estado> estadoColumn;

    @FXML
    private Button agregarButton;

    @FXML
    private Button actualizarButton;

    @FXML
    private Button limpiarButton;

    @FXML
    private Button borrarButton;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField contraseniaTextField;

    @FXML
    private TextField dniTextField;

    @FXML
    private CheckBox estadoCheckBox;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private Button volverButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        ControllersMethods.alinearTabla(idColumn);
        ControllersMethods.alinearTabla(nombreColumn);
        ControllersMethods.alinearTabla(apellidoColumn);
        ControllersMethods.alinearTabla(dniColumn);
        ControllersMethods.alinearTabla(usuarioColumn);
        ControllersMethods.alinearTabla(fechaCreacionColumn);
        ControllersMethods.alinearTabla(estadoColumn);

        tableUsuario.setItems(observableList);
    }
    public Usuario.Estado obtenerEstado(){
        if(estadoCheckBox.isSelected()){
            return Usuario.Estado.Activo;
        }else{
            return Usuario.Estado.Inactivo;
        }
    }
    public boolean obtenerBooleanEstado(Usuario usuario){
        if(usuario.getEstado().equals(Usuario.Estado.Activo)){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void agregar() {
        if (ControllersMethods.checkCampos(usuarioTextField,nombreTextField,
                apellidoTextField,contraseniaTextField, dniTextField)) {
            try {
                Usuario usuario = new Usuario(nombreTextField.getText(),apellidoTextField.getText(),dniTextField.getText(),
                        usuarioTextField.getText(),contraseniaTextField.getText(),obtenerEstado());
                if (observableList.contains(usuario)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("No se puede agregar un usuario ya existente");
                    alert.showAndWait();
                    actualizarButton.setText("Actualizar");
                    actualizarButton.setOnAction(event -> actualizar());
                }else {
                    observableList.add(usuario);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error en los campos,reviselos");
                alert.setContentText("Algunos de los campos es " +
                        "incorrecto revise de no poner letras en los campos con numeros");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        limpiar();
    }
    @Override
    public void actualizar(){
        Usuario usuario = tableUsuario.getSelectionModel().getSelectedItem();
        if(usuario != null) {
            nombreTextField.setText(usuario.getNombre());
            apellidoTextField.setText(usuario.getApellido());
            dniTextField.setText(usuario.getDni());
            usuarioTextField.setText(usuario.getUsuario());
            contraseniaTextField.setText(usuario.getContrasenia());
            estadoCheckBox.setSelected(obtenerBooleanEstado(usuario));

            actualizarButton.setText("Guardar");

            actualizarButton.setOnAction(event -> {
                modificar(usuario);
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error para actualizar");
            alert.setContentText("Ningun usuario seleccionado");
            alert.showAndWait();
        }
    }
    @Override
    public void modificar(Usuario usuario){

        if(ControllersMethods.checkCampos(usuarioTextField,nombreTextField,
                apellidoTextField,contraseniaTextField, dniTextField)) {
            usuario.setNombre(nombreTextField.getText());
            usuario.setApellido(apellidoTextField.getText());
            usuario.setDni(dniTextField.getText());
            usuario.setUsuario(usuarioTextField.getText());
            usuario.setContrasenia(usuarioTextField.getText());
            usuario.setEstado(obtenerEstado());
            observableList.set(observableList.indexOf(usuario),usuario);
        }
        limpiar();
        actualizarButton.setText("Actualizar");
        actualizarButton.setOnAction(event -> actualizar());
    }
    @Override
    public void borrar(){
        observableList.remove(tableUsuario.getSelectionModel().getSelectedItem());
    }
    @Override
    public void limpiar(){
        ControllersMethods.limpiar(usuarioTextField, nombreTextField, apellidoTextField,
                contraseniaTextField, dniTextField);
        estadoCheckBox.setSelected(false);
    }
    public void volver(){
        Jackson.serializar(observableList,pathJson);//se trabaja con cache
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/tp_final_lab3/Views/ADMIN_Seleccion.fxml"));
            Stage stage = (Stage) volverButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
