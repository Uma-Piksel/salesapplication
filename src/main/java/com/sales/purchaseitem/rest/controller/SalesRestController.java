package com.sales.purchaseitem.rest.controller;

import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import com.sales.purchaseitem.rest.service.PurchaseItemsReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author piksel
 */

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {

    @Autowired
    private PurchaseItemsReceiptService receiptService;

    @RequestMapping(value = "/purchaseItems/receipt", method = RequestMethod.GET)
    public ResponseEntity<PurchaseReceipt> getReceiptForPurchaseItems(@RequestBody PurchaseItemList purchaseItemList) {
        PurchaseReceipt receipt = receiptService.getReceipt(purchaseItemList);
        return new ResponseEntity<PurchaseReceipt>(receipt, HttpStatus.OK);
    }
}
