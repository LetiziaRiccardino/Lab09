
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	try {
    		String a= txtAnno.getText();
    		int anno= Integer.parseInt(a);
    		if(anno<1816 || anno>2016) {
    			txtResult.setText("Inserire un anno compreso tra 1816 e 2016");
    			return;
    		}
    		
    		model.creaGrafo(anno);
    		txtResult.appendText("# Vertici: "+ model.nVertici()+"\n");
    		txtResult.appendText("# Archi: "+ model.nArchi()+"\n");
    		
    		txtResult.appendText("vertice e grado del vertice: \n");
    		for(Country c: this.model.statiConfinanti().keySet() ) {
    			txtResult.appendText("Stato: "+c.getCcode()+" grado: "+this.model.statiConfinanti().get(c)+"\n");
    		}
    		
    		txtResult.appendText("\n Numero componenti connesse nel grafo: "+ this.model.getComponenteConnessa());
    		
    		
    		
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserire un anno compreso tra 1816 e 2016");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
