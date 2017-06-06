package de.metas.fresh.picking.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.adempiere.ad.trx.api.ITrx;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_BPartner;

import de.metas.fresh.picking.form.IFreshPackingItem;
import de.metas.fresh.picking.service.IPackingContext;
import de.metas.handlingunits.HUTestHelper;
import de.metas.handlingunits.IHUContext;
import de.metas.handlingunits.model.I_M_HU;
import de.metas.handlingunits.model.I_M_HU_PI;
import de.metas.handlingunits.model.I_M_HU_PI_Item;
import de.metas.handlingunits.model.I_M_HU_PI_Item_Product;
import de.metas.handlingunits.model.X_M_HU_PI_Version;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/*
 * #%L
 * de.metas.fresh.base
 * %%
 * Copyright (C) 2017 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

@UtilityClass
public class HU2PackingItemTestCommons
{
	final int COUNT_IFCOs_Per_Palet = 5;
	final int COUNT_Tomatoes_Per_IFCO = 10;

	public HUTestHelper commonCreateHUTestHelper()
	{
		return new HUTestHelper()
		{
			@Override
			protected String createAndStartTransaction()
			{
				// no transaction by default
				return ITrx.TRXNAME_None;
			}
		};
	}

	public I_M_HU_PI_Item_Product createHuDefIFCO(@NonNull final HUTestHelper helper)
	{
		final I_M_HU_PI huDefIFCO = helper.createHUDefinition(HUTestHelper.NAME_IFCO_Product, X_M_HU_PI_Version.HU_UNITTYPE_TransportUnit);

		final I_M_HU_PI_Item itemMA = helper.createHU_PI_Item_Material(huDefIFCO);
		final I_M_HU_PI_Item_Product huPiItemProduct = helper.assignProduct(itemMA, helper.pTomato, BigDecimal.valueOf(COUNT_Tomatoes_Per_IFCO), helper.uomEach);

		return huPiItemProduct;
	}

	/**
	 * Creates a {@link I_M_HU_PI} for a loading unit, links the given transport unit's {@code tuHuDef} and returns the respective {@link I_M_HU_PI_Item} (which therefore has type=HU).
	 * 
	 * @param helper
	 * @param tuHuDef
	 * @return
	 */
	public I_M_HU_PI_Item createHuDefPalet(@NonNull final HUTestHelper helper, @NonNull final I_M_HU_PI_Item_Product tuHuDef)
	{
		final I_M_HU_PI huDefPalet = helper.createHUDefinition(HUTestHelper.NAME_Palet_Product, X_M_HU_PI_Version.HU_UNITTYPE_LoadLogistiqueUnit);
		final I_C_BPartner bpartner = null;
		final I_M_HU_PI_Item huPiItem = helper.createHU_PI_Item_IncludedHU(
				huDefPalet,
				tuHuDef.getM_HU_PI_Item().getM_HU_PI_Version().getM_HU_PI(),
				BigDecimal.valueOf(COUNT_IFCOs_Per_Palet),
				bpartner);

		return huPiItem;
	}

	/**
	 * Creates as many palets as are needed to contain the given qty within IFCOs.
	 * 
	 * @param helper
	 * @param luHuDef
	 * @param tuHuDef
	 * @param qtyToLoad
	 * @return
	 */
	public List<I_M_HU> createLUs(
			@NonNull final HUTestHelper helper,
			@NonNull final I_M_HU_PI_Item luHuDef,
			@NonNull final I_M_HU_PI_Item_Product tuHuDef,
			final int qtyToLoad)
	{
		if (qtyToLoad % COUNT_Tomatoes_Per_IFCO != 0)
		{
			throw new AdempiereException("QtyToLoad shall be multiple of " + COUNT_Tomatoes_Per_IFCO + " else method assertValidShipmentScheduleLUTUAssignments will fail");
		}
		final IHUContext huContext = helper.createMutableHUContextForProcessing(ITrx.TRXNAME_None);
		final List<I_M_HU> luHUs = helper.createLUs(huContext, luHuDef, tuHuDef, new BigDecimal(qtyToLoad));
		
		return luHUs;
	}
	
	
	public List<I_M_HU> createTUs(
			@NonNull final HUTestHelper helper,
			@NonNull final I_M_HU_PI_Item_Product tuHuDef,
			final int qtyToLoad)
	{
		if (qtyToLoad % COUNT_Tomatoes_Per_IFCO != 0)
		{
			throw new AdempiereException("QtyToLoad shall be multiple of " + COUNT_Tomatoes_Per_IFCO + " else method assertValidShipmentScheduleLUTUAssignments will fail");
		}

		final IHUContext huContext = helper.createMutableHUContextForProcessing(ITrx.TRXNAME_None);
		final BigDecimal qtyToLoadBD = BigDecimal.valueOf(qtyToLoad);
		final List<I_M_HU> hus = helper.createHUs(huContext, tuHuDef.getM_HU_PI_Item().getM_HU_PI_Version().getM_HU_PI(), helper.pTomato, qtyToLoadBD, helper.uomEach);

		return hus;
	}
	
	/**
	 * Creates HU to Packing Items Allocator (i.e. class under test)
	 */
	public HU2PackingItemsAllocator createHU2PackingItemsAllocator(
			@NonNull final IPackingContext packingContext,
			@NonNull final IFreshPackingItem itemToPack)
	{
		final HU2PackingItemsAllocator hu2PackingItemsAllocator = new HU2PackingItemsAllocator();
		hu2PackingItemsAllocator.setItemToPack(itemToPack);
		hu2PackingItemsAllocator.setPackingContext(packingContext);
		return hu2PackingItemsAllocator;
	}
}
