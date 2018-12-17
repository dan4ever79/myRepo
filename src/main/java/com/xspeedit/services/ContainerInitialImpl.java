package com.xspeedit.services;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Initial implementation related to initial algorithm (debut de l'enonce)
 */
public class ContainerInitialImpl extends AbstractContainer implements ContainerOptimizerIntf {
    @Override
    List<String> applyOptimisation(List<Integer> items) {

        List<String> result = new ArrayList<>();

        int sumItems = 0;
        StringBuilder curContainer = new StringBuilder();
        for (int item : items) {
            if (item + sumItems <= MAX_SIZE_CONTAINER) {
                curContainer.append(item);
                sumItems += item;
            } else {
                result.add(curContainer.toString());
                curContainer = new StringBuilder();
                curContainer.append(item);
                sumItems = item;

            }
        }
        if (StringUtils.isNotBlank(curContainer.toString())) {
            result.add(curContainer.toString());
        }
        return result;
    }
}
