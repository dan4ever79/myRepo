package com.xspeedit.services;

import com.xspeedit.exception.ItemException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Test
public class ContainerTest {


    private final static int [] items_enonce =
            {
                1, 6, 3, 8, 4, 1, 6, 8, 9, 5, 2, 5, 7, 7, 3     // valeur initiale
            };

    private final static Logger LOG = LoggerFactory.getLogger(ContainerTest.class);

    /**
     * Given a list of items
     * Then return a list of bag of items (not optimized) - initial
     */
    @Test(description =
            "Given a list of items\n" +
                    "Then return a list of bag of items (not optimized) - initial"
    )
    public void givenItemsThenReturnNotListOptimised() throws ItemException {
        ContainerOptimizerIntf o = new ContainerInitialImpl();
        List<String> containers = o.process(items_enonce);
        LOG.info("Cha\u00eene d'articles en entr\u00e9e: {}", items_enonce);
        LOG.info("Robot actuel  : {} - nombre de sacs({})", StringUtils.join(containers, "/"), containers.size());
        Assert.assertEquals(containers.size(), 10);
    }


    /**
     * Given a list of items
     * Then return a list of bag of items (not optimized) and apply also optimized
     * Assert that the result of new algo for containers should be lower than initial
     */
    @Test(
            description = "Given a list of items\n" +
                    "Then return a list of bag of items (not optimized) and apply also optimized\n" +
                    "Assert that the result of new algo for containers should be lower than initial"
    )
    public void givenItemsThenReturnListOptimisedAndShouldBeLowerThanInitial() throws ItemException {
        ContainerOptimizerIntf o = new ContainerInitialImpl();
        long startInitial = System.currentTimeMillis();
        List<String> containers = o.process(items_enonce);
        long endInitial = System.currentTimeMillis();
        LOG.info("Cha\u00eene d'articles en entr\u00e9e: {}", items_enonce);
        LOG.info("Robot actuel\t: {} - nombre de sacs({}) - takes {}ms", StringUtils.join(containers, "/"), containers.size(), endInitial - startInitial);

        ContainerOptimizerIntf optimizeImpl = new ContainerTamiImpl();
        long startOptimized = System.currentTimeMillis();
        List<String> containersOptimized = optimizeImpl.process(items_enonce);
        long endOptimized = System.currentTimeMillis();

        LOG.info("Robot optimis\u00e9\t: {} - nombre de sacs({}) - takes {}ms", StringUtils.join(containersOptimized, "/"), containersOptimized.size(), endOptimized - startOptimized);
        Assert.assertTrue(containersOptimized.size() < containers.size(), "optimized containers should be lower than initial");

    }


    /**
     * Given a list of items with 1 items above max length item (9)
     * Then return a list of bag of items (not optimized) and apply also optimized
     * Assert that the result of new algo for containers should be lower than initial
     */
    private final static int [] items_enonce_with_exception = {
         1, 2, 10, 2, 1
    };

    @Test(expectedExceptions = {
            ItemException.class
            }
            ,
            description = "Given a list of items with 1 items above max length item (9)\n" +
                    "Then return a list of bag of items (not optimized) and apply also optimized\n" +
                    "Assert that the result of new algo for containers should be lower than initial"
    )
    public void givenItemsWhenOneItemAboveMaxSizeItemThenAssertException() throws ItemException {
        ContainerOptimizerIntf o = new ContainerInitialImpl();
        long startInitial = System.currentTimeMillis();
        List<String> containers = o.process(items_enonce_with_exception);
        Assert.fail("exception should be thrown");
    }



    /**
     * Given a list of items
     * Then return a list of bag of items (not optimized) and apply also optimized
     * Assert that the result of new algo for containers should be lower than initial
     */
    @Test(
            description = "Given a list of items (benchmark)\n" +
                    "Then return a list of bag of items (not optimized) and apply also optimized\n" +
                    "Assert that the result of new algo for containers should be lower than initial"
    )
    public void givenItemsThenReturnListOptimisedAndShouldBeLowerThanInitialAndBenchmark() throws ItemException {
        final int MAX_ITEMS = 100;

        List<Integer> items_benchmark = new ArrayList<>();
        for (int i = 0; i < MAX_ITEMS; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(AbstractContainer.MIN_VALUE_INCLUDED, AbstractContainer.MAX_VALUE_EXCLUDED);
            items_benchmark.add(randomNum);
        }

        int [] itemsPrimitive = items_benchmark.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        ContainerOptimizerIntf o = new ContainerInitialImpl();
        long startInitial = System.currentTimeMillis();
        List<String> containers = o.process(itemsPrimitive);
        long endInitial = System.currentTimeMillis();
        LOG.info("Cha\u00eene d'articles en entr\u00e9e: {}", items_benchmark);
        LOG.info("Robot actuel\t: {} - nombre de sacs({}) - takes {}ms", StringUtils.join(containers, "/"), containers.size(), endInitial - startInitial);

        ContainerOptimizerIntf optimizeImpl = new ContainerTamiImpl();
        long startOptimized = System.currentTimeMillis();
        List<String> containersOptimized = optimizeImpl.process(itemsPrimitive);
        long endOptimized = System.currentTimeMillis();

        LOG.info("Robot optimis\u00e9\t: {} - nombre de sacs({}) - takes {}ms", StringUtils.join(containersOptimized, "/"), containersOptimized.size(), endOptimized - startOptimized);
        Assert.assertTrue(containersOptimized.size() < containers.size(), "optimized containers should be lower than initial");

    }


}
