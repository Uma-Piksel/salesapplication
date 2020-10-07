package com.sales.purchaseitem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Model data object holds the purchaseItem details.
 *
 * @author piksel
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PurchaseItem implements Serializable {

    private String quantity;
    private String itemName;
    private Double itemPrice;
    @JsonIgnore
    private Double salesTax;
}
