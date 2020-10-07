package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author piksel
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesTaxEvaluatorTest {

    @InjectMocks
    private SalesTaxEvaluator salesTaxEvaluator;

    @Before
    public void setUpBefore() {
        ReflectionTestUtils.setField(salesTaxEvaluator, "basicTaxRate", 10d);
        ReflectionTestUtils.setField(salesTaxEvaluator, "importedTaxRate", 5d);
    }

    @Test
    public void getApplicableSalesTaxReturns0TaxForExemptedItem(){
        Set<String> exemptedItems = getExemptedItems();
        PurchaseItem item = buildPurchaseItem("book", "14.99");

        Double result = salesTaxEvaluator.getApplicableSalesTax(item,  exemptedItems);
        assertThat(result, is(0d));
    }

    @Test
    public void getApplicableSalesTaxReturns10TaxForNonExemptedItem(){
        Set<String> exemptedItems = getExemptedItems();
        PurchaseItem item = buildPurchaseItem("music CD", "14.99");

        Double result = salesTaxEvaluator.getApplicableSalesTax(item,  exemptedItems);
        assertThat(result, is(10d));
    }

    @Test
    public void getApplicableSalesTaxReturns15dTaxForNonExemptedItemAndImportedItem(){
        Set<String> exemptedItems = getExemptedItems();
        PurchaseItem item = buildPurchaseItem("imported box of chocolate", "14.99");

        Double result = salesTaxEvaluator.getApplicableSalesTax(item,  exemptedItems);
        assertThat(result, is(15d));
    }

    private PurchaseItem buildPurchaseItem(String name, String price) {
        return PurchaseItem.builder().itemName(name).itemPrice(new Double(price)).quantity("1").build();
    }

    private Set<String> getExemptedItems() {
        Set<String> exemptedItems = Sets.newHashSet();
        exemptedItems.add("book");
        exemptedItems.add("chocolate bar");
        exemptedItems.add("packet of headache pills");
        return exemptedItems;
    }
}