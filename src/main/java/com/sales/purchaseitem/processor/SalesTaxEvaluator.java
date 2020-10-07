package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * calculates the tax discount
 */

@Component
public class SalesTaxEvaluator {

    private static final String IMPORTED = "imported";
    private static final double TAX_NOT_APPLICABLE = 0d;

    @Value("${basic.tax.rate}")
    private Double basicTaxRate;

    @Value("${imported.tax.rate}")
    private Double importedTaxRate;

    public Double getApplicableSalesTax(PurchaseItem purchaseItem, Set<String> exemptedItems) {
        Double importedTaxRate = getImportedTaxRate(purchaseItem.getItemName());
        Double basicTaxRate = getBasicTaxRate(purchaseItem, exemptedItems);
        return basicTaxRate + importedTaxRate;
    }

    private Double getImportedTaxRate(String name) {
        return name.contains(IMPORTED) ? importedTaxRate : TAX_NOT_APPLICABLE;
    }

    private Double getBasicTaxRate(PurchaseItem purchaseItem, Set<String> exemptedItems) {
        boolean isBasicExempted = isTaxExempted(exemptedItems, purchaseItem.getItemName());
        if (purchaseItem.getItemName().contains(IMPORTED) && !isBasicExempted) {
            return basicTaxRate;
        }
        return isBasicExempted ? TAX_NOT_APPLICABLE : basicTaxRate;
    }

    private boolean isTaxExempted(Set<String> exemptedItems, String name) {
        int count = findMatching(exemptedItems, name);
        return count > 0;
    }

    private int findMatching(Set<String> exemptedItems, String name) {
        int count = 0 ;
        for (String exemptedItem : exemptedItems) {
              if(name.contains(exemptedItem)) {
                  count ++ ;
                  break;
              }
        }
        return count;
    }

}
