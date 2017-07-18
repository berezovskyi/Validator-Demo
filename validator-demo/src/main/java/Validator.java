
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.weso.rdf.PrefixMap;
import es.weso.rdf.jena.RDFAsJenaModel;
import es.weso.schema.Result;
import es.weso.schema.Schema;
import es.weso.schema.Schemas;
import scala.Option;
import scala.util.Try;

public class Validator {
	
	public static final Option<String> OPTION_NONE = Option.apply(null);
	private final static Logger LOGGER = LoggerFactory.getLogger(Validator.class);
	

	public static CharSequence streamAsCharSequence(final InputStream inputStream) throws IOException {
		return IOUtils.toString(inputStream, Charset.forName("UTF-8"));
	}
	
	public static CharSequence load(final String resourcePath, final String msg) throws IOException {
		InputStream schemaStream = Validator.class.getResourceAsStream(resourcePath);
		CharSequence streamAsCharSequence;
		try {
			streamAsCharSequence = streamAsCharSequence(schemaStream);
		} catch (IOException e) {
			LOGGER.error(msg, e);
			throw e;
		}
		return streamAsCharSequence;
	}
	
	public static Schema getSchema(String fileName) {
	    try {
			CharSequence charSequence = Validator.load(fileName, "Error reading schema from stream");
			Try<Schema> schemaTry = Schemas.fromString(charSequence, "TURTLE", "SHACLex", OPTION_NONE);

			if (schemaTry.isSuccess()) {
				return schemaTry.get();
			} else {
				throw new IllegalArgumentException("Failed to parse the schema");
			}
	    } catch (IOException e) {
	    	throw new IllegalArgumentException("Failed to read the schema file", e);
	    }
	}

	public static Result validate(final RDFAsJenaModel rdf, final Schema schema) {
		PrefixMap nodeMap = rdf.getPrefixMap();
		PrefixMap shapesMap = schema.pm();
		final String triggerMode = "TargetDecls";
		final Option<String> node = OPTION_NONE;
		final Option<String> shape = OPTION_NONE;
		return schema.validate(rdf, triggerMode, node, shape, nodeMap, shapesMap);
	}
		
	
	public static Result validate(RDFAsJenaModel resourceAsRDFReader, RDFAsJenaModel shapeAsRDFReader) {
		Try<Schema> schemaTry = Schemas.fromRDF(shapeAsRDFReader, "SHACLex");
		if (schemaTry.isSuccess()) {
			Schema schema = schemaTry.get();
			return validate(resourceAsRDFReader, schema);
		} else {
			throw new IllegalArgumentException("Malformed schema");
		}
	}
	
	public static Result validate(Model resourceAsModel, Model shapeAsModel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, DatatypeConfigurationException {
		RDFAsJenaModel resourceAsRDFReader = new RDFAsJenaModel(resourceAsModel);
		RDFAsJenaModel shapeAsRDFReader = new RDFAsJenaModel(shapeAsModel); 
		return Validator.validate(resourceAsRDFReader, shapeAsRDFReader);
	}
}

