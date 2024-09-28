package nl.tudelft.tbm.eeni.owl2java.plugin;


import nl.tudelft.tbm.eeni.owl2java.JenaGenerator;
import nl.tudelft.tbm.eeni.owlstructure.processor.PropertyRangeSimplifier;
import nl.tudelft.tbm.eeni.owlstructure.utils.OntologyUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "generate-source", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateSourcesMojo extends AbstractMojo {

    @Parameter(property = "owlFile", required = true)
    private File owlFile;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources", property = "outputDir", required = true)
    private File outputDirectory;

    @Parameter(property = "basePackage", required = true)
    private String basePackage;

    @Parameter(defaultValue="${project}", readonly=true, required=true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        if (!owlFile.exists()) {
           throw new MojoExecutionException("owlFile not found: " + owlFile);
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try {
            // Load example ontology
            OntModel ontModel = OntologyUtils.loadOntology(owlFile.toString());

            // Simplify the definition of property ranges
            // This is necessary because owl2java chokes on complex range
            // definitions (i.e. those containing anonymous classes)
            (new PropertyRangeSimplifier()).process(ontModel);

            // Generate classes that provide access to ontology instances
            JenaGenerator generator = new JenaGenerator();
            generator.generate(ontModel, outputDirectory.toString(), basePackage);

            // Add generated source directory to the project's source directories
            project.addCompileSourceRoot(outputDirectory.getPath());

            getLog().info("Source files generated at " + outputDirectory.getPath());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
