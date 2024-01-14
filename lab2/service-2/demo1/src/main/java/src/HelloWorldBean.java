package src;


import jakarta.ejb.Stateless;

@Stateless
public class HelloWorldBean implements HelloWorld {
    @Override
    public String getHelloWorld() {
        return "Welcome to EJB Tutorial!";
    }
}