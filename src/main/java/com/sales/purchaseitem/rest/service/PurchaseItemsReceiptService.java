package com.sales.purchaseitem.rest.service;

import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;

/**
 * service responsible for processing receipt
 */
public interface PurchaseItemsReceiptService {

    PurchaseReceipt getReceipt(PurchaseItemList purchaseItemList);
}
