package com.sales.purchaseitem.processor;

import com.sales.purchaseitem.model.PurchaseItem;
import com.sales.purchaseitem.model.PurchaseItemList;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;


/**
 * @author piksel
 */
@RunWith(MockitoJUnitRunner.class)
public class PurchaseItemSaleTaxesCalculatorTest {

    @InjectMocks
    private PurchaseItemSaleTaxesCalculator saleTaxCalculator;

    @Mock
    private SalesTaxEvaluator salesTaxEvaluator;

    @Before
    public void setUpBefore() {
        ReflectionTestUtils.setField(saleTaxCalculator, "taxExemptedItems", "book, chocolate bar, pack of headache pills");
    }

    @Test
    public void testSaleTaxCalculatorAppliesBasicTaxRateToItem() {
        PurchaseItem item = buildPurchaseItem("music CD", "14.99");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item)).build();
        when(salesTaxEvaluator.getApplicableSalesTax(any(PurchaseItem.class), anySet())).thenReturn(10d);

        PurchaseItemList result = saleTaxCalculator.processSaleTaxesForPurchaseItem(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getPurchaseItems().size(), is(1));
        PurchaseItem purchaseItem = result.getPurchaseItems().get(0);
        assertThat(purchaseItem.getItemName(), is("music CD"));
        assertThat(purchaseItem.getItemPrice(), is(14.99));

        assertThat(purchaseItem.getSalesTax(), is(1.5));
    }

    @Test
    public void testSaleTaxCalculatorDoesNotApplyBasicTaxRateToItemIfAnItemIsExempted() {
        PurchaseItem item = buildPurchaseItem("chocolate", "0.85");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item)).build();
        when(salesTaxEvaluator.getApplicableSalesTax(any(PurchaseItem.class), anySet())).thenReturn(0d);

        PurchaseItemList result = saleTaxCalculator.processSaleTaxesForPurchaseItem(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getPurchaseItems().size(), is(1));
        PurchaseItem purchaseItem = result.getPurchaseItems().get(0);
        assertThat(purchaseItem.getItemName(), is("chocolate"));
        assertThat(purchaseItem.getItemPrice(), is(0.85));
        assertThat(purchaseItem.getSalesTax(), equalTo(0.00));
    }

    @Test
    public void testSaleTaxesCalculatorAppliesOnlyImportedTaxAnItemWhenItemIsImportedAndExempted() {
        PurchaseItem item = buildPurchaseItem("imported chocolate", "10.00");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item)).build();
        when(salesTaxEvaluator.getApplicableSalesTax(any(PurchaseItem.class), anySet())).thenReturn(5d);

        PurchaseItemList result = saleTaxCalculator.processSaleTaxesForPurchaseItem(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getPurchaseItems().size(), is(1));
        PurchaseItem purchaseItem = result.getPurchaseItems().get(0);
        assertThat(purchaseItem.getItemName(), is("imported chocolate"));
        assertThat(purchaseItem.getItemPrice(), is(10.00));
        assertThat(purchaseItem.getSalesTax(), is(0.5));
    }

    @Test
    public void testSaleTaxesCalculatorAppliesBothImportedTaxAndBasicTaxRateForAnItemWhenItemIsImportedAndNotExempted() {
        PurchaseItem item = buildPurchaseItem("imported perfume", "27.99");
        PurchaseItemList purchaseItemList = PurchaseItemList.builder().purchaseItems(Lists.newArrayList(item)).build();
        when(salesTaxEvaluator.getApplicableSalesTax(any(PurchaseItem.class), anySet())).thenReturn(15d);

        PurchaseItemList result = saleTaxCalculator.processSaleTaxesForPurchaseItem(purchaseItemList);

        assertThat(result, notNullValue());
        assertThat(result.getPurchaseItems().size(), is(1));
        PurchaseItem purchaseItem = result.getPurchaseItems().get(0);
        assertThat(purchaseItem.getItemName(), is("imported perfume"));
        assertThat(purchaseItem.getItemPrice(), is(27.99));
        assertThat(purchaseItem.getSalesTax(), is(4.2));
    }

    @Test
    public void applyRoundingRuleRoundsUptoNearestFiveCentsForGivenPrice() {

        Double result1 = saleTaxCalculator.applyRoundingRule(4.1985);
        assertThat(result1, is(4.2));

        Double result2 = saleTaxCalculator.applyRoundingRule(0.5625);
        assertThat(result2, is(0.6));

        Double result3 = saleTaxCalculator.applyRoundingRule(1.8990);
        assertThat(result3, is(1.9));

        Double result4 = saleTaxCalculator.applyRoundingRule(0.50);
        assertThat(result4, is(0.50));

        Double result5 = saleTaxCalculator.applyRoundingRule(1.499);
        assertThat(result5, is(1.5));

        Double result6 = saleTaxCalculator.applyRoundingRule(1.34);
        assertThat(result6, is(1.35));

        Double result7 = saleTaxCalculator.applyRoundingRule(7.125);
        assertThat(result7, is(7.15));

    }

    private PurchaseItem buildPurchaseItem(String name, String price) {
        return PurchaseItem.builder().itemName(name).itemPrice(new Double(price)).quantity("1").build();
    }

}