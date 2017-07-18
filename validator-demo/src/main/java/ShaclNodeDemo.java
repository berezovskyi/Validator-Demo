import java.lang.reflect.InvocationTargetException;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;

import es.weso.schema.Result;

public class ShaclNodeDemo {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DatatypeConfigurationException {


		// Load the main data model
		Model dataModel = ModelFactory.createDefaultModel();
		dataModel.read(ShaclNodeDemo.class.getResourceAsStream("/shaclTargetNodeData.ttl"), "urn:dummy", FileUtils.langTurtle);
		dataModel.write(System.out, "TURTLE");

		// Load the main data model
		Model shapeModel = ModelFactory.createDefaultModel();
		shapeModel.read(ShaclNodeDemo.class.getResourceAsStream("/shaclTargetNodeShape.ttl"), "urn:dummy", FileUtils.langTurtle);
		shapeModel.write(System.out, "TURTLE");

		Result vr =Validator.validate(dataModel, shapeModel);
		//errors should be empty since the targetNode does not exist
		System.out.println(vr.errors());
	}
}
