package org.springframework.samples.petclinic.system;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link WelcomeController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WelcomeController.class)
public class WelcomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        MvcResult result = mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("welcome")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content.matches("(?s).*/resources/css/petclinic-[0-9a-f]+.css.*"))
                .as("Expected to match\n%s", content).isTrue();
    }

    @Test
    public void testStylesheet() throws Exception {
        MvcResult result = mockMvc.perform(get("/resources/css/petclinic.css"))
                .andExpect(status().isOk()).andReturn();
        String css = result.getResponse().getContentAsString();
        assertThat(
                css.matches("(?s).*/resources/images/spring-logo-dataflow-[0-9a-f]+.png.*"))
                        .as("Expected to match\n%s", css).isTrue();
    }

}
