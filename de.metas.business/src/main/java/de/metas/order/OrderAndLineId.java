package de.metas.order;

import lombok.NonNull;
import lombok.Value;

/*
 * #%L
 * de.metas.business
 * %%
 * Copyright (C) 2018 metas GmbH
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

@Value
public class OrderAndLineId
{
	public static OrderAndLineId ofRepoIdsOrNull(final int orderRepoId, final int orderLineRepoId)
	{
		if (orderRepoId <= 0 || orderLineRepoId <= 0)
		{
			return null;
		}
		return ofRepoIds(orderRepoId, orderLineRepoId);
	}
	
	public static OrderAndLineId ofRepoIds(final int orderRepoId, final int orderLineRepoId)
	{
		return new OrderAndLineId(OrderId.ofRepoId(orderRepoId), OrderLineId.ofRepoId(orderLineRepoId));
	}

	public static OrderAndLineId of(final OrderId orderId, final OrderLineId orderLineId)
	{
		return new OrderAndLineId(orderId, orderLineId);
	}

	public static int getOrderRepoIdOr(final OrderAndLineId orderAndLineId, final int defaultValue)
	{
		return orderAndLineId != null ? orderAndLineId.getOrderRepoId() : defaultValue;
	}

	public static int getOrderLineRepoIdOr(final OrderAndLineId orderAndLineId, final int defaultValue)
	{
		return orderAndLineId != null ? orderAndLineId.getOrderLineRepoId() : defaultValue;
	}

	OrderId orderId;
	OrderLineId orderLineId;

	private OrderAndLineId(@NonNull final OrderId orderId, @NonNull final OrderLineId orderLineId)
	{
		this.orderId = orderId;
		this.orderLineId = orderLineId;
	}

	public int getOrderRepoId()
	{
		return orderId.getRepoId();
	}

	public int getOrderLineRepoId()
	{
		return orderLineId.getRepoId();
	}

}
