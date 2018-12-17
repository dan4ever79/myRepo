package com.xspeedit.services;

import com.xspeedit.model.ContainerTamisDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class utilities to optimize items in container
 * Algorithm use : TAMIS por filtrer les elements selon les optimisations codees
 *
 */
public class ContainerTamiImpl extends AbstractContainer implements ContainerOptimizerIntf {

    private final static Logger LOG = LoggerFactory.getLogger(ContainerTamiImpl.class);

    /**
     * Remove items that correspond to bagOptimisationSignature
     * @param itemsBySize
     * @param bagTami
     */
    private void removeItemsIn(Map<Integer,Integer> itemsBySize, final String bagTami) {
        for (int pos = 0; pos < bagTami.length(); pos++) {
            int keyName = ContainerTamisDefinition.LENGTH - pos;
            int key = Integer.valueOf("" + bagTami.charAt(pos));
            itemsBySize.put(keyName, itemsBySize.get(keyName) - key);
        }
    }


    /**
     * Vectorize as matrix items according to tami to match (invert)
     * @param currentTami
     * @param itemsBySize
     * @return
     */
    private String vectorizeItemsBySize(final String currentTami, Map<Integer,Integer> itemsBySize) {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < currentTami.length(); i++) {
            char c = currentTami.charAt(i);
            int val = Integer.valueOf("" + c);
            if (val > 0 && itemsBySize.get(ContainerTamisDefinition.LENGTH - i) > 0) {
                if (val < itemsBySize.get(ContainerTamisDefinition.LENGTH - i)) {
                    tmp.append("1");
                } else {
                    tmp.append(itemsBySize.get(ContainerTamisDefinition.LENGTH - i));
                }
            } else {
                tmp.append("0");
            }
        }
        return tmp.toString();
    }


    /**
     * Find out which best composition is, regarding datas
     * @param keyReversed
     * @param itemsBySize
     * @return best composition
     */
    private String getBestBagOptimisation(List<Integer> keyReversed, Map<Integer,Integer> itemsBySize) {
        for (String bestOptimisation : ContainerTamisDefinition.DEFINITIONS) {
            String val = vectorizeItemsBySize(bestOptimisation, itemsBySize);
            int valItems = Integer.valueOf(val);
            if (Integer.valueOf(bestOptimisation) <= valItems) {
                removeItemsIn(itemsBySize, bestOptimisation);
                return bestOptimisation;
            }
        }
        return null;
    }

    private String listItemsInBag(final String composition) {
        if (composition == null) {
            return null;
        }
        StringBuilder vals = new StringBuilder();
        for (int i = 0; i < composition.length(); i++) {
            int c = Integer.parseInt("" + composition.charAt(i));
            int size = ContainerTamisDefinition.LENGTH - i;
            while (c-- > 0) {
                vals.append(size);
            }
        }
        return vals.toString();
    }


    /**
     * Count item by size (size will be the key)
     * @param items
     * @return map
     */
    private Map<Integer, Integer> countItemBySize(final List<Integer> items) {
        Map<Integer, Integer> itemsBySize = new ConcurrentHashMap<>();
        for (int item : items) {
            if (!itemsBySize.containsKey(item)) {
                itemsBySize.put(item, Integer.valueOf(0));
            }
            itemsBySize.put(item, itemsBySize.get(item) + 1);
        }

        // comptage par size
        if (LOG.isTraceEnabled()) {
            for (int item : itemsBySize.keySet()) {
                LOG.trace("item by size {} : nb items : {}", item, itemsBySize.get(item));
            }

        }
        return itemsBySize;
    }



    @Override
    public List<String> applyOptimisation(List<Integer> items) {
        Map<Integer,Integer> itemsBySize = countItemBySize(items);

        List<String> bagsCompleted = new ArrayList<>();
        List<Integer> keysReversed = new ArrayList<>(itemsBySize.keySet());
        Collections.reverse(keysReversed);

        // on complete en appliquant la tremi
        String bagFound = getBestBagOptimisation(keysReversed, itemsBySize);
        while (bagFound != null) {
            LOG.trace("we add a new bag : {}", bagFound);
            bagsCompleted.add(listItemsInBag(bagFound));
            bagFound = getBestBagOptimisation(keysReversed, itemsBySize);
        }
        if (bagFound != null) {
            bagsCompleted.add(listItemsInBag(bagFound));
        }

        // on affiche s'il reste des elements (cela voudrait dire qu'il manque des definitions de tamis
        for (int item : itemsBySize.keySet()) {
            if (itemsBySize.get(item) > 0) {
                LOG.warn("rest item by size {} : nb items : {} - missing tamis definitions -- need to be completed", item, itemsBySize.get(item));
            }
        }
        return bagsCompleted;
    }


}
