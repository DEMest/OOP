package ru.nsu.smolin.checker.tools;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public final class JunitXmlParser {
    private JunitXmlParser() {}

    public record Counts(int passed, int failed, int skipped) {
        public Counts add(Counts o) {
            return new Counts(passed + o.passed, failed + o.failed, skipped + o.skipped);
        }
    }

    public static Counts parseDir(Path dir) {
        if (!Files.isDirectory(dir)) return new Counts(0, 0, 0);
        Counts total = new Counts(0, 0, 0);
        try (Stream<Path> s = Files.list(dir)) {
            for (Path p : (Iterable<Path>) s.filter(f -> f.toString().endsWith(".xml"))::iterator) {
                total = total.add(parseFile(p));
            }
        } catch (IOException e) {
            return total;
        }
        return total;
    }

    private static Counts parseFile(Path file) {
        try {
            var f = DocumentBuilderFactory.newInstance();
            f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = f.newDocumentBuilder().parse(file.toFile());
            NodeList cases = doc.getElementsByTagName("testcase");
            int passed = 0, failed = 0, skipped = 0;
            for (int i = 0; i < cases.getLength(); i++) {
                Element c = (Element) cases.item(i);
                if (c.getElementsByTagName("failure").getLength() > 0
                    || c.getElementsByTagName("error").getLength() > 0) {
                    failed++;
                } else if (c.getElementsByTagName("skipped").getLength() > 0) {
                    skipped++;
                } else {
                    passed++;
                }
            }
            return new Counts(passed, failed, skipped);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            return new Counts(0, 0, 0);
        }
    }
}
