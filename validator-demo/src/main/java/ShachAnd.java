import java.lang.reflect.InvocationTargetException;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;

import es.weso.schema.Result;

public class ShachAnd {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DatatypeConfigurationException {


		// Load the main data model
		Model dataModel = ModelFactory.createDefaultModel();
		dataModel.read(ShachAnd.class.getResourceAsStream("/shaclAnd.ttl"), "urn:dummy", FileUtils.langTurtle);
		dataModel.write(System.out, "TURTLE");

		// Load the main data model
		Model shapeModel = ModelFactory.createDefaultModel();
		shapeModel.read(ShachAnd.class.getResourceAsStream("/shaclAndShape.ttl"), "urn:dummy", FileUtils.langTurtle);
		shapeModel.write(System.out, "TURTLE");

		Result vr =Validator.validate(dataModel, shapeModel);
		//should show error on node ex:InvalidInstance but it is empty
		System.out.println(vr.errors());
	}

}
