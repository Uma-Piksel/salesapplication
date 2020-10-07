package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import com.sales.purchaseitem.model.PurchaseItemList;
import com.sales.purchaseitem.model.PurchaseReceipt;
import com.sales.purchaseitem.model.PurchaseReceiptItem;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author piksel
 */
@RunWith(MockitoJUnitRunner.class)
public class PurchaseItemsTotalCalculatorTest {


    @InjectMocks
    private PurchaseItemsTotalCalculator totalCalculator;

    @Test
    public void calculateReceiptsWithTotalCostAndTotalSalesTaxPaidForGivenPurchaseItems() {
        PurchaseItem item1 = buildPurchaseItem("music CD", "14.99", "1", "1.50");
        PurchaseItem item2 = buildPurchaseItem("book", "12.49", "1", "0.00");
        PurchaseItem item3 = buildPurchaseItem("chocolate bar", "0.85", "1", "0.00");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item1, item2, item3)).build();

        PurchaseReceipt result = totalCalculator.calculateTotal(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getReceiptItems(), org.hamcrest.Matchers.hasSize(3));
        assertThat(result.getTotalCostPaid(), is(29.83));
        assertThat(result.getTotalTaxPaid(), is(1.50));
        PurchaseReceiptItem receiptItem = result.getReceiptItems().get(0);
        assertThat(receiptItem.getItemName(), is("music CD"));
        assertThat(receiptItem.getItemPrice(), is(16.49));
    }

    private PurchaseItem buildPurchaseItem(String name, String price, String quantity, String salesTax) {
        return PurchaseItem.builder().itemName(name).itemPrice(new Double(price)).quantity(quantity).salesTax(new Double(salesTax)).build();
    }

}