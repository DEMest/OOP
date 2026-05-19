package ru.nsu.smolin.checker.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class JunitXmlParserTest {
    @Test
    void countsPassedFailedSkippedAcrossFiles(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("a.xml"), """
            <?xml version="1.0"?>
            <testsuite tests="3" failures="1" errors="0" skipped="1">
              <testcase name="ok"/>
              <testcase name="bad"><failure/></testcase>
              <testcase name="meh"><skipped/></testcase>
            </testsuite>
            """);
        Files.writeString(dir.resolve("b.xml"), """
            <?xml version="1.0"?>
            <testsuite tests="2" failures="0" errors="1" skipped="0">
              <testcase name="ok2"/>
              <testcase name="boom"><error/></testcase>
            </testsuite>
            """);

        JunitXmlParser.Counts c = JunitXmlParser.parseDir(dir);

        assertEquals(2, c.passed());
        assertEquals(2, c.failed());
        assertEquals(1, c.skipped());
    }

    @Test
    void emptyDirReturnsZeros(@TempDir Path dir) {
        JunitXmlParser.Counts c = JunitXmlParser.parseDir(dir);
        assertEquals(0, c.passed());
        assertEquals(0, c.failed());
        assertEquals(0, c.skipped());
    }
}
