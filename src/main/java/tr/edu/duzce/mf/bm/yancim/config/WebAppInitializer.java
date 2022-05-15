package tr.edu.duzce.mf.bm.yancim.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import tr.edu.duzce.mf.bm.yancim.core.utilities.filter.RequestFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
        // TODO: Burası yeni eklendi.
        dispatcherServlet.setInitParameter("throwExceptionIfNoHandlerFound", "true");

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        servletContext.addFilter("characterEncodingFilter",
                characterEncodingFilter).addMappingForUrlPatterns(null, false, "/*");
        servletContext.addFilter("requestFilter", new RequestFilter()).addMappingForUrlPatterns(null, false, "/*");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("tr.edu.duzce.mf.bm.yancim.config");
        return context;
    }
}
