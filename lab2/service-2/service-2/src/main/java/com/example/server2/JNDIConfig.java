package com.example.server2;






import src.HelloWorld;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JNDIConfig {
    public static HelloWorld helloWorldBean(){
        System.out.println("Hi!");
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put("jboss.naming.client.ejb.context", true);
        jndiProps.put(Context.URL_PKG_PREFIXES,"org.jboss.ejb.client.naming");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8089");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        jndiProps.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_PASS_CREDENTIALS", "false");
        jndiProps.put(Context.SECURITY_PRINCIPAL, "labsoa");
        jndiProps.put(Context.SECURITY_CREDENTIALS, "isomuchLoveSOA3");
        try {
            final Context context = new InitialContext(jndiProps);
            return  (HelloWorld) context.lookup("ejb:/demo1-1.0-SNAPSHOT/HelloWorldBean!src.HelloWorld");
        } catch (NamingException e){
            e.printStackTrace();
        }
        return null;
    }
}
