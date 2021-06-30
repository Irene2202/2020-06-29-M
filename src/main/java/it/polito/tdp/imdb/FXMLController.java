/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Adiacente;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer anno=boxAnno.getValue();
    	
    	if(anno==null) {
    		txtResult.setText("SELEZIONARE UN ANNO");
    		return;
    	}
    	
    	model.creaGrafo(anno);
    	txtResult.appendText("Grafo creato.");
    	txtResult.appendText("\n#vertici: "+model.getGrafo().vertexSet().size());
    	txtResult.appendText("\n#archi: "+model.getGrafo().edgeSet().size());	
    	
    	boxRegista.getItems().clear();
    	boxRegista.getItems().addAll(model.getGrafo().vertexSet());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	txtResult.clear();
    	
    	if(model.getGrafo()==null) {
    		txtResult.setText("GRAFO NON ANCORA CREATO");
    		return;
    	}
    	
    	Director d=boxRegista.getValue();
    	if(d==null) {
    		txtResult.setText("SELEZIONARE UN REGISTA");
    		return;
    	}
    	
    	txtResult.setText("REGISTI ADIACENTI A: "+d.toString());
    	for(Adiacente a: model.getRegistiAdiacenti(d)) {
    		txtResult.appendText("\n"+a.getD()+" - #attori condivisi: "+a.getPeso());
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	txtResult.clear();
    	
    	if(model.getGrafo()==null) {
    		txtResult.setText("GRAFO NON ANCORA CREATO");
    		return;
    	}
    	
    	Director d=boxRegista.getValue();
    	if(d==null) {
    		txtResult.setText("SELEZIONARE UN REGISTA");
    		return;
    	}
    	
    	String pesoMaxS=txtAttoriCondivisi.getText();
    	int pesoMax=0;
    	try {
    		pesoMax=Integer.parseInt(pesoMaxS);
    	} catch (NumberFormatException e) {
    		txtResult.setText("INSERIRE UN NUMERO INTERO NEL CAMPO 'ATTORI CONDIVISI'");
    		return;
    	}
    	
    	txtResult.setText("Percorso massimo a partire da "+d+" di peso minore o uguale a "+pesoMax);
    	for(Director di:model.getRegistiAffini(d, pesoMax))
    		txtResult.appendText("\n"+di);
    	
    	if(model.getPesoPercorso()!=-1) {
    		txtResult.appendText("\nPeso Totale del percorso: "+model.getPesoPercorso());
    	} else {
    		txtResult.appendText("\nPercorso Migliore non trovato");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    	boxAnno.getItems().add(2004);
    	boxAnno.getItems().add(2005);
    	boxAnno.getItems().add(2006);

    }
    
}
