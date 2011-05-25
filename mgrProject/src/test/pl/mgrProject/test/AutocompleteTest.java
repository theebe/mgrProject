package pl.mgrProject.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class AutocompleteTest extends SeamTest {

	@Test
	public void test_autocomplete() throws Exception {
		new FacesRequest("/autocomplete.xhtml") {
			@Override
			protected void invokeApplication() {
				//call action methods here
				invokeMethod("#{Autocomplete.autocomplete}");
			}
		}.run();
	}
}
