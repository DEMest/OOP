package demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloTest {
    @Test void greets() { assertEquals("hi", Hello.greet()); }
}
