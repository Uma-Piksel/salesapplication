package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import com.sales.purchaseitem.model.PurchaseReceiptItem;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptProcessorTest {

    @InjectMocks
    private ReceiptProcessor receiptProcessor;

    @Mock
    private PurchaseItemSaleTaxesCalculator taxesCalculator;

    @Mock
    private PurchaseItemsTotalCalculator totalCalculator;


    @Test
    public void testProcessReceiptSuccessfull() {
        PurchaseItem item1 = buildPurchaseItem("book", "12.49");
        PurchaseItem item2 = buildPurchaseItem("music CD", "14.99");
        PurchaseItem item3 = buildPurchaseItem("chocolate", "0.85");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item1, item2, item3)).build();
        PurchaseItemList expectedPurchaseItemListWithSalesTax = getPurchaseItemListWithSalesTax();
        when(taxesCalculator.processSaleTaxesForPurchaseItem(purchaseItemList)).thenReturn(expectedPurchaseItemListWithSalesTax);
        PurchaseReceipt expectedPurchaseReceipt = buildPurchaseReceipt();
        when(totalCalculator.calculateTotal(expectedPurchaseItemListWithSalesTax)).thenReturn(expectedPurchaseReceipt);

        PurchaseReceipt result = receiptProcessor.processReceipt(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getTotalCostPaid(), is(29.83));
        assertThat(result.getTotalTaxPaid(), is(1.5));
        assertThat(result.getReceiptItems().size(), is(3));
    }

    private PurchaseReceipt buildPurchaseReceipt() {
        PurchaseReceiptItem item1 = PurchaseReceiptItem.builder().itemName("music CD").itemPrice(12.49).quantity("1").build();
        PurchaseReceiptItem item2 = PurchaseReceiptItem.builder().itemName("music CD").itemPrice(16.49).quantity("1").build();
        PurchaseReceiptItem item3 = PurchaseReceiptItem.builder().itemName("chocolate").itemPrice(0.85).quantity("1").build();
        return PurchaseReceipt.builder().receiptItems(Lists.newArrayList(item1, item2, item3)).totalCostPaid(29.83).totalTaxPaid(1.5).build();
    }

    private PurchaseItemList getPurchaseItemListWithSalesTax() {
        PurchaseItem item1 = buildPurchaseItem("book", "12.49", 0.0);
        PurchaseItem item2 = buildPurchaseItem("music CD", "14.99", 1.50);
        PurchaseItem item3 = buildPurchaseItem("chocolate", "0.85", 0.0);
        return PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item1, item2, item3)).build();
    }

    private PurchaseItem buildPurchaseItem(String name, String price, Double salesTax) {
        return PurchaseItem.builder().itemName(name).itemPrice(new Double(price)).quantity("1").salesTax(salesTax).build();
    }

    private PurchaseItem buildPurchaseItem(String name, String price) {
        return PurchaseItem.builder().itemName(name).itemPrice(new Double(price)).quantity("1").build();
    }

}