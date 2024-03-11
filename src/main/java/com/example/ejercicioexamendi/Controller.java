package com.example.ejercicioexamendi;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Controller {

    @FXML
    private RadioButton radiusHombre;
    @FXML
    private ToggleGroup sexo;
    @FXML
    private RadioButton radiusMujer;
    @FXML
    private TextField txtPeso;
    @FXML
    private RadioButton radiusKilos;
    @FXML
    private ToggleGroup tipoPeso;
    @FXML
    private RadioButton radiusLibras;
    @FXML
    private TextField txtEstatura;
    @FXML
    private TextField txtEdad;
    @FXML
    private ComboBox<String> comboActividad;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblGastoEnergetico;
    @FXML
    private TextField txtNombre;

    @FXML
    private void initialize() {

        comboActividad.getItems().addAll("Sedentario", "Moderado", "Activo", "Muy activo");

        comboActividad.getSelectionModel().selectFirst();
    }


    @FXML
    private void calcularGastoEnergetico() {
        try {
            if (txtPeso.getText().isEmpty() || txtEstatura.getText().isEmpty() || txtEdad.getText().isEmpty()|| txtNombre.getText().isEmpty()) {
                lblGastoEnergetico.setText("Por favor, complete todos los campos.");
                return;
            }


            double peso = Double.parseDouble(txtPeso.getText());
            double estatura = Double.parseDouble(txtEstatura.getText());
            int edad = Integer.parseInt(txtEdad.getText());

            if (peso <= 0 || peso > 200 || estatura <= 0 || estatura > 250 || edad <= 0 || edad > 100) {
                lblGastoEnergetico.setText("Por favor, ingrese valores válidos.");
                return;
            }


            if (radiusLibras.isSelected()) {
                peso *= 0.453592;
            }

            double gastoEnergetico;
            double ger;


            if (radiusHombre.isSelected()) {
                gastoEnergetico = 66.473 + (13.751 * peso) + (5.0033 * estatura) - (6.755 * edad);
                ger = gastoEnergetico;

                switch (comboActividad.getValue()) {
                    case "Sedentario":
                        gastoEnergetico *= 1.3;
                        break;
                    case "Moderado":
                        gastoEnergetico *= 1.6;
                        break;
                    case "Activo":
                        gastoEnergetico *= 1.7;
                        break;
                    case "Muy activo":
                        gastoEnergetico *= 2.1;
                        break;
                    default:
                        break;
                }
            } else {
                gastoEnergetico = 655.0955 + (9.463 * peso) + (1.8496 * estatura) - (4.6756 * edad);
                ger = gastoEnergetico;

                switch (comboActividad.getValue()) {
                    case "Sedentario":
                        gastoEnergetico *= 1.3;
                        break;
                    case "Moderado":
                        gastoEnergetico *= 1.5;
                        break;
                    case "Activo":
                        gastoEnergetico *= 1.6;
                        break;
                    case "Muy activo":
                        gastoEnergetico *= 1.9;
                        break;
                    default:
                        break;
                }
            }


            lblGastoEnergetico.setText(String.format("GER: %.2f kcal/día, GET: %.2f kcal/día", ger, gastoEnergetico));

        } catch (NumberFormatException e) {

            lblGastoEnergetico.setText("Por favor, ingrese números válidos en los campos de peso, estatura y edad.");
        }
    }

    @FXML
    public void verInforme(ActionEvent actionEvent) throws SQLException,JRException {




        Integer idCliente = 2;

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/informacioncliente", "root", "");

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("id", idCliente);


        var jasperPrint = JasperFillManager.fillReport("informacionclientes.jasper", hashMap, connection);



        JRViewer viewer = new JRViewer(jasperPrint);

        JFrame frame = new JFrame("Listado de Clientes");
        frame.getContentPane().add(viewer);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);

        System.out.print("Done!");

        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("clientes.pdf"));
        exp.setConfiguration(new SimplePdfExporterConfiguration());
        exp.exportReport();

        System.out.print("Done!");

    }

}

