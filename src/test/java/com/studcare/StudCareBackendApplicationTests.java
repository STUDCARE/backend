package com.studcare;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class StudCareBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest(CapturedOutput output) {
		StudCareBackendApplication.main(new String[]{});
		assertTrue(output.getOut().contains("Started StudCareBackendApplication"));
	}
}
