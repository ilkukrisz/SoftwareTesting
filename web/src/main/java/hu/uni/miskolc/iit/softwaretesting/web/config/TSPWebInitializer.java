package hu.uni.miskolc.iit.softwaretesting.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class TSPWebInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext annotationConfigCtx = new AnnotationConfigWebApplicationContext();

        servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"))
                .addMappingForUrlPatterns(null, false, "/*");

        annotationConfigCtx.register(LibrarianContext.class);
        annotationConfigCtx.register(ReaderContext.class);
        annotationConfigCtx.setServletContext(servletContext);

        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet(
                "dispatcher", new DispatcherServlet((WebApplicationContext) annotationConfigCtx)
        );

        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");
    }
}
