package com.sales.purchaseitem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Receipt holds the purchase item receipt.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PurchaseReceipt implements Serializable {

    @Builder.Default
    private List<PurchaseReceiptItem> receiptItems = new ArrayList<>();

    private Double totalTaxPaid;

    private Double totalCostPaid;
}
