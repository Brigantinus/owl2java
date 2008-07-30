package de.incunabulum.owl2java.core.generator.db4o;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import de.incunabulum.owl2java.core.Db4oGenerator;
import de.incunabulum.owl2java.core.generator.AbstractWriter;
import de.incunabulum.owl2java.core.model.jmodel.JClass;
import de.incunabulum.owl2java.core.model.jmodel.JModel;
import de.incunabulum.owl2java.core.model.jmodel.utils.NamingUtils;
import de.incunabulum.owl2java.core.model.xsd.XsdMapTestData;

public class Db4oWriter extends AbstractWriter {
	
	static Log log = LogFactory.getLog(Db4oWriter.class);
	
	public static String templateDirDb4oClassBased = "db4oTemplatesCB";
	public static String templateDirDb4oIfaceBased = "db4oTemplatesIB";
	
	private static String templateDirCurrent = "";

	private String instanceClassName;
	private int generationType;
	private boolean generateMergeCode;
	
	public static String getTemplatePath(String templateName) {
		return "/" + templateDirCurrent + "/" + templateName;
	}

	
	@Override
	public void generate(JModel model, String baseDir, String basePackage) {
		this.baseDir = baseDir;
		this.jmodel = model;
		this.basePackage = basePackage;

		log.info("");
		log.info("Writing JModel to java");

		// create the package structure
		createPackageDirectories();

		// init the templating engine
		if (generationType == Db4oGenerator.ClassBasedGeneration) {
			templateDirCurrent = templateDirDb4oClassBased;
			initVelocityEngine(templateDirDb4oClassBased);
			createClasses();
		} else {
			templateDirCurrent = templateDirDb4oIfaceBased;
			initVelocityEngine(templateDirDb4oIfaceBased);
			// TODO: createInterfaces missing
			createClasses();
		}
		
		// write instance merge code
		if (generateMergeCode)
			createInstanceMergeCode();
		
		log.info("Done");
		
	}

	protected void createInstanceMergeCode() {
		log.info("Creating instance handling stuff");
		Db4oMergeCodeWriter vWriter = new Db4oMergeCodeWriter(vEngine, getBaseVelocityContext());
		vWriter.setInstanceName(instanceClassName);
		vWriter.setToolsPackage(toolsPackage);
		vWriter.writeInstance(jmodel, baseDir, basePackage);		
	}
	
	
	
	protected VelocityContext getBaseVelocityContext() {
		// add some default stuff to our context. These are reused over all writers
		VelocityContext vContext = new VelocityContext();
		Calendar c = Calendar.getInstance();
		vContext.put("now", SimpleDateFormat.getInstance().format(c.getTime()));
		vContext.put("pkgBase", basePackage);
		vContext.put("jmodel", jmodel);
		vContext.put("instanceName", instanceClassName);
		vContext.put("instancePkg", NamingUtils.getJavaPackageName(basePackage, toolsPackage));

		XsdMapTestData xsdMap = new XsdMapTestData();
		vContext.put("xsdMap", xsdMap);
		return vContext;
	}

	
	@SuppressWarnings("unchecked")
	protected void createClasses() {
		log.info("Creating java classes");
		Db4oClassWriter clsWriter = new Db4oClassWriter(vEngine, getBaseVelocityContext());

		Iterator<JClass> clsIt = jmodel.listJClasses().iterator();
		while (clsIt.hasNext()) {
			JClass cls = clsIt.next();
			clsWriter.writeClass(jmodel, cls, baseDir);
		}

	}
	public void setInstanceClassName(String instanceClassName) {
		this.instanceClassName = instanceClassName;
	}

	public void setGenerationType(int generationType) {
		this.generationType = generationType;
	}

	public void setGenerateMergeCode(boolean generateMergeCode) {
		this.generateMergeCode = generateMergeCode;
	}


}
