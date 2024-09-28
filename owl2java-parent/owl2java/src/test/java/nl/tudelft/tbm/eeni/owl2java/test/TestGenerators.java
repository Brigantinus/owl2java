package nl.tudelft.tbm.eeni.owl2java.test;

import java.net.URISyntaxException;
import java.util.Date;

import nl.tudelft.tbm.eeni.owl2java.JenaGenerator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestGenerators {

	Logger log = LoggerFactory.getLogger(TestGenerators.class);

	@Test
	void testJenaGeneratorFull() throws URISyntaxException {
		Date startDate = new Date();

		JenaGenerator gen = new JenaGenerator();

		String uri = "classpath:/test/owl4java.owl";
		gen.generate(uri, "/tmp/testOut", "jenatestFull");

		// report
		String report = report = gen.getStatistics();
		assertNotNull(report);
		log.info(report);

		Date stopDate = new Date();
		long elapse = stopDate.getTime() - startDate.getTime();
		log.info("Test finished ({} ms)", elapse);
	}

	@Test
	void testJenaGeneratorSimple() {
		Date startDate = new Date();

		JenaGenerator gen = new JenaGenerator();

		String uri = "classpath:/test/owl4java.owl";
		gen.generate(uri, "/tmp/testOut", "jenatestSimple");

		String report = gen.getStatistics();
		assertNotNull(report);
		log.info(report);

		Date stopDate = new Date();
		long elapse = stopDate.getTime() - startDate.getTime();
		log.info("Test finished ({} ms)", elapse);
	}
}
