package com.sales.purchaseitem.rest.service;

import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;

/**
 * @author piksel
 */
public interface PurchaseItemsReceiptService {

    PurchaseReceipt getReceipt(PurchaseItemList purchaseItemList);
}
