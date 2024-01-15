package com.example.soalab2server1.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import src.service.operation.OrganizationOperation;
import src.service.operation.ServiceOperation;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration @Slf4j
public class ConfigEjb {

    @Bean
    public Context context() throws NamingException {
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put("jboss.naming.client.ejb.context", true);
        jndiProps.put(Context.URL_PKG_PREFIXES,"org.jboss.ejb.client.naming");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8089");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_PASS_CREDENTIALS", "false");
        jndiProps.put(Context.SECURITY_PRINCIPAL, "labsoa");
        jndiProps.put(Context.SECURITY_CREDENTIALS, "isomuchLoveSOA3");
        return new InitialContext(jndiProps);
    }

    @Bean
    public OrganizationOperation organizationOperationOperation(Context context) throws NamingException {
        return (OrganizationOperation) context.lookup("ejb:/demo1-1.0-SNAPSHOT/OrganizationService!src.service.operation.OrganizationOperation");
    }
    @Bean
    public ServiceOperation serviceOperation(Context context) throws NamingException {
        return (ServiceOperation) context.lookup("ejb:/demo1-1.0-SNAPSHOT/WorkerService!src.service.operation.ServiceOperation");
    }
}
