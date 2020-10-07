package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import com.sales.purchaseitem.model.PurchaseItemList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Calculates the sales tax for the purchase item.
 *
 * @author piksel
 */
@Component
@RequiredArgsConstructor
public class PurchaseItemSaleTaxesCalculator {
    private static final double TAX_NOT_APPLICABLE = 0d;

    private final SalesTaxEvaluator salesTaxEvaluator;

    @Value("${tax.exempted.items}")
    private String taxExemptedItems;

    public PurchaseItemList processSaleTaxesForPurchaseItem(PurchaseItemList purchaseItemList) {
        Set<String> exemptedItems = StringUtils.commaDelimitedListToSet(taxExemptedItems);
        purchaseItemList.getPurchaseItems().forEach(purchaseItem -> calculateTaxRateForItem(purchaseItem, exemptedItems));
        return purchaseItemList;
    }

    private void calculateTaxRateForItem(PurchaseItem purchaseItem, Set<String> exemptedItems) {
        Double taxToBeApplied = salesTaxEvaluator.getApplicableSalesTax(purchaseItem, exemptedItems);
        Double itemSalesTax = calculateItemSalesTax(purchaseItem.getItemPrice(), taxToBeApplied);
        Double itemSalesTaxAfterRounding = applyRoundingRule(itemSalesTax);
        purchaseItem.setSalesTax(itemSalesTaxAfterRounding);
    }

    private Double calculateItemSalesTax(Double taxToBeApplied, Double actualItemPrice) {
        return taxToBeApplied == TAX_NOT_APPLICABLE ? taxToBeApplied : (taxToBeApplied * actualItemPrice) / 100d;
    }

    //accessmodifier is public because to unit test this function
    public double applyRoundingRule(double value) {
        double ceiling = Math.ceil(value * 20);
        if (ceiling == 0) {
            return 0;
        }
        return ceiling / 20;
    }

}
