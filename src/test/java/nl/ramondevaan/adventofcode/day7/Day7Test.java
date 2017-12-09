package nl.ramondevaan.adventofcode.day7;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Day7Test {
    private Day7 day7;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        if(day7 != null) {
            return;
        }

        day7 = Day7.create(
                Paths.get(this.getClass().getResource(
                        "/Day7.txt"
                ).toURI()));
    }

    @Test
    public void exercise1() {
        Assert.assertEquals("hmvwl", day7.rootName());
    }

    @Test
    public void exercise2() {
        Assert.assertEquals(1853, day7.unbalanced());
    }
}
