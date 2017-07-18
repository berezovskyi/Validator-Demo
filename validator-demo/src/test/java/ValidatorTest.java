import es.weso.schema.ErrorInfo;
import es.weso.schema.Result;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

/**
 * Created on 2017-07-18
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.1
 */
public class ValidatorTest {

    public static <T> java.util.List<T> convert(scala.collection.Seq<T> seq) {
        return scala.collection.JavaConverters.seqAsJavaList(seq);
    }

    @Test
    public void testAnd() throws Exception {
        Model dataModel = ModelFactory.createDefaultModel();
        dataModel.read(ValidatorTest.class.getResourceAsStream("/shaclAnd.ttl"), "urn:dummy",
                FileUtils.langTurtle);
//        dataModel.write(System.out, "TURTLE");

        // Load the main data model
        Model shapeModel = ModelFactory.createDefaultModel();
        shapeModel.read(ValidatorTest.class.getResourceAsStream("/shaclAndShape.ttl"), "urn:dummy",
                FileUtils.langTurtle);
//        shapeModel.write(System.out, "TURTLE");

        Result vr = Validator.validate(dataModel, shapeModel);
        final List<ErrorInfo> errors = convert(vr.errors());

        //should show error on node ex:InvalidInstance but it is empty
        assertThat(errors).isNotEmpty();
    }

    @Test
    public void testNode() throws Exception {
        // Load the main data model
        Model dataModel = ModelFactory.createDefaultModel();
        dataModel.read(ValidatorTest.class.getResourceAsStream("/shaclTargetNodeData.ttl"),
                "urn:dummy", FileUtils.langTurtle);
//        dataModel.write(System.out, "TURTLE");

        // Load the main data model
        Model shapeModel = ModelFactory.createDefaultModel();
        shapeModel.read(ValidatorTest.class.getResourceAsStream("/shaclTargetNodeShape.ttl"),
                "urn:dummy", FileUtils.langTurtle);
//        shapeModel.write(System.out, "TURTLE");

        Result vr = Validator.validate(dataModel, shapeModel);
        final List<ErrorInfo> errors = convert(vr.errors());

        //errors should be empty since the targetNode does not exist
        assertThat(errors).isEmpty();
    }
}
