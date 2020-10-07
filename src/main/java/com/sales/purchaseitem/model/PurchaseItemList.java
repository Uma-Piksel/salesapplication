package com.sales.purchaseitem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/*
 * Model data objects holds the list of purchased Items.
 * @author piksel
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseItemList implements Serializable {

    private List<PurchaseItem> purchaseItems;
}
