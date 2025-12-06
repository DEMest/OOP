package ru.nsu.smolin.block;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.table.Text;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListBlockTest {

    @Test
    void unorderedListSuccess() {
        ListItem i1 = new ListItem(List.of(new Text.Plain("one")));
        ListItem i2 = new ListItem(List.of(new Text.Plain("two")));

        ListBlock list = new ListBlock(
                ListBlock.Type.UNORDERED,
                List.of(i1, i2)
        );

        assertEquals("- one\n- two", list.toMarkdown());
    }

    @Test
    void orderedListSuccess() {
        ListItem i1 = new ListItem(List.of(new Text.Plain("a")));
        ListItem i2 = new ListItem(List.of(new Text.Plain("b")));

        ListBlock list = new ListBlock(
                ListBlock.Type.ORDERED,
                List.of(i1, i2)
        );

        assertEquals("1. a\n2. b", list.toMarkdown());
    }

    @Test
    void taskListSuccess() {
        ListItem t1 = new ListItem(List.of(new Text.Plain("done")), true);
        ListItem t2 = new ListItem(List.of(new Text.Plain("todo")), false);

        ListBlock tasks = new ListBlock(
                ListBlock.Type.TASKS,
                List.of(t1, t2)
        );

        assertEquals("- [x] done\n- [ ] todo", tasks.toMarkdown());
    }
}
