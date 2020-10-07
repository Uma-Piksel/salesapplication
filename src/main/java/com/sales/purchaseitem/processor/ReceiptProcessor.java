package com.sales.purchaseitem.processor;


import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Responsible for processing the receipt items.
 *
 * @author piksel
 */
@Component
@RequiredArgsConstructor
public class ReceiptProcessor {

    private final PurchaseItemSaleTaxesCalculator taxesCalculator;

    private final PurchaseItemsTotalCalculator totalCalculator;

    public PurchaseReceipt processReceipt(PurchaseItemList inputPurchaseItemList) {
        PurchaseItemList processedItemList = taxesCalculator.processSaleTaxesForPurchaseItem(inputPurchaseItemList);
        return totalCalculator.calculateTotal(processedItemList);
    }

}
