package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import com.sales.purchaseitem.model.PurchaseReceiptItem;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Calculates the total cost and total tax paid for the given purchased items.
 */
@Component
public class PurchaseItemsTotalCalculator {
    private static final double TAX_NOT_APPLICABLE = 0d;


    public PurchaseReceipt calculateTotal(PurchaseItemList purchaseItemList) {
        Double totalCostPaid = 0d;
        Double totalTaxPaid = 0d;

        List<PurchaseReceiptItem> receiptItems = Lists.newArrayList();
        for (PurchaseItem item : purchaseItemList.getPurchaseItems()) {
            Double priceIncludingTaxWithQuantity = getItemPriceIncludingTaxAndQuantity(item);
            receiptItems.add(buildReceiptItem(item, priceIncludingTaxWithQuantity));
            totalCostPaid = totalCostPaid + priceIncludingTaxWithQuantity;
            totalTaxPaid = totalTaxPaid + item.getSalesTax();
        }
        return buildPurchaseReceipt(receiptItems, totalCostPaid, totalTaxPaid);
    }

    private PurchaseReceipt buildPurchaseReceipt(List<PurchaseReceiptItem> receiptItems, Double totalCostPaid, Double totalTaxPaid) {
        return PurchaseReceipt.builder()
                .receiptItems(receiptItems)
                .totalCostPaid(format(totalCostPaid))
                .totalTaxPaid(format(totalTaxPaid))
                .build();
    }

    private double format(Double input) {
        return Double.parseDouble(String.format("%.2f", input));
    }

    private Double getItemPriceIncludingTaxAndQuantity(PurchaseItem purchaseItem) {
        Double priceIncludingTax = addSalesTaxToPurchasedItem(purchaseItem.getItemPrice(), purchaseItem.getSalesTax());
        Double priceIncludingTaxWithQuantity = priceIncludingTax * (new Double(purchaseItem.getQuantity()));
        return format(priceIncludingTaxWithQuantity);
    }

    private Double addSalesTaxToPurchasedItem(Double purchaseItemPrice, Double itemSalesTax) {
        if (itemSalesTax == TAX_NOT_APPLICABLE) {
            return purchaseItemPrice;
        }
        return purchaseItemPrice + itemSalesTax;
    }

    private PurchaseReceiptItem buildReceiptItem(PurchaseItem purchaseItem, Double itemPriceByQuantity) {
        return PurchaseReceiptItem.builder()
                .quantity(purchaseItem.getQuantity())
                .itemName(purchaseItem.getItemName())
                .itemPrice(itemPriceByQuantity)
                .build();
    }
}
