package com.sales.purchaseitem.rest.service;

import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import com.sales.purchaseitem.processor.ReceiptProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implementation receipt processor.
 */
@Service
@RequiredArgsConstructor
public class PurchaseItemsReceiptServiceImpl implements PurchaseItemsReceiptService {

    private final ReceiptProcessor receiptProcessor;

    @Override
    public PurchaseReceipt getReceipt(PurchaseItemList purchaseItemList) {

        return receiptProcessor.processReceipt(purchaseItemList);
    }
}
