package de.lmu.ifi.dbs.elki.database.ids.generic;
/*
This file is part of ELKI:
Environment for Developing KDD-Applications Supported by Index-Structures

Copyright (C) 2011
Ludwig-Maximilians-Universität München
Lehr- und Forschungseinheit für Datenbanksysteme
ELKI Development Team

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.AbstractCollection;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

import de.lmu.ifi.dbs.elki.database.ids.ArrayDBIDs;
import de.lmu.ifi.dbs.elki.database.ids.DBID;
import de.lmu.ifi.dbs.elki.database.ids.DBIDs;

/**
 * View on an ArrayDBIDs masked using a BitMask for efficient mask changing.
 * 
 * @author Erich Schubert
 * 
 * @apiviz.uses de.lmu.ifi.dbs.elki.database.ids.DBIDs
 */
public class MaskedDBIDs extends AbstractCollection<DBID> implements DBIDs, Collection<DBID> {
  /**
   * Data storage
   */
  protected ArrayDBIDs data;

  /**
   * The bitmask used for masking
   */
  protected BitSet bits;

  /**
   * Flag whether to iterator over set or unset values.
   */
  protected boolean inverse = false;

  /**
   * Constructor.
   * 
   * @param data Data
   * @param bits Bitset to use as mask
   * @param inverse Flag to inverse the masking rule
   */
  public MaskedDBIDs(ArrayDBIDs data, BitSet bits, boolean inverse) {
    super();
    this.data = data;
    this.bits = bits;
    this.inverse = inverse;
  }

  @Override
  public Iterator<DBID> iterator() {
    if(inverse) {
      return new InvItr();
    }
    else {
      return new Itr();
    }
  }

  @Override
  public Collection<DBID> asCollection() {
    return this;
  }

  @Override
  public int size() {
    if(inverse) {
      return data.size() - bits.cardinality();
    }
    else {
      return bits.cardinality();
    }
  }

  /**
   * Iterator over set bits
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  protected class Itr implements Iterator<DBID> {
    /**
     * Next position.
     */
    private int pos;

    /**
     * Constructor
     */
    protected Itr() {
      this.pos = bits.nextSetBit(0);
    }

    @Override
    public boolean hasNext() {
      return (pos >= 0) && (pos < data.size());
    }

    @Override
    public DBID next() {
      DBID cur = data.get(pos);
      pos = bits.nextSetBit(pos + 1);
      return cur;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Iterator over unset elements.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  protected class InvItr implements Iterator<DBID> {
    /**
     * Next unset position.
     */
    private int pos;

    /**
     * Constructor
     */
    protected InvItr() {
      this.pos = bits.nextClearBit(0);
    }

    @Override
    public boolean hasNext() {
      return (pos >= 0) && (pos < data.size());
    }

    @Override
    public DBID next() {
      DBID cur = data.get(pos);
      pos = bits.nextClearBit(pos + 1);
      return cur;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public boolean add(DBID e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }
}
