package com.xspeedit.services;

import com.xspeedit.exception.ItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements ContainerOptimizerIntf  {

    public final static int MIN_VALUE_INCLUDED = 1;
    public final static int MAX_VALUE_EXCLUDED = 10;

    public final static int MAX_SIZE_CONTAINER = 10;

    public final static Logger LOG = LoggerFactory.getLogger(AbstractContainer.class);

    /**
     * Compute and put items in container with a maximised size to 10
     * @param items
     * @return a list of string - each string contains the items
     * @throws ItemException
     */
    public List<String> process(final int [] items) throws ItemException {
        List<Integer> originalItems = new ArrayList<>();
        for (int item : items) {
            if (item < MIN_VALUE_INCLUDED || item >= MAX_VALUE_EXCLUDED ) {
                throw new ItemException();
            }
            originalItems.add(item);
        }
        return applyOptimisation(originalItems);
    }

    /**
     * method to implement for each algorithm implementation
     * @param items
     */
    abstract List<String> applyOptimisation(List<Integer> items);

}
