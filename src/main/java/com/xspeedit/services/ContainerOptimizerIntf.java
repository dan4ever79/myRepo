package com.xspeedit.services;

import com.xspeedit.exception.ItemException;

import java.util.List;

public interface ContainerOptimizerIntf {

    /**
     * Compute and put items in container with a maximised size to 10
     * @param items
     * @return list of each container
     * @throws ItemException
     */
    public List<String> process(final int [] items) throws ItemException;


}
