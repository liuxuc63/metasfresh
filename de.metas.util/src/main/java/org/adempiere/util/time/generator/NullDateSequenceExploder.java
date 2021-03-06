package org.adempiere.util.time.generator;

import java.time.LocalDate;

/*
 * #%L
 * de.metas.util
 * %%
 * Copyright (C) 2015 metas GmbH
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

import java.util.Collection;
import java.util.Collections;

import lombok.Value;

@Value
public final class NullDateSequenceExploder implements IDateSequenceExploder
{
	public static final NullDateSequenceExploder instance = new NullDateSequenceExploder();

	private NullDateSequenceExploder()
	{
	}

	@Override
	public Collection<LocalDate> explodeForward(final LocalDate date)
	{
		return Collections.singleton(date);
	}

	@Override
	public Collection<LocalDate> explodeBackward(final LocalDate date)
	{
		return Collections.singleton(date);
	}
}
