/* **************************************************************************************
 * 																						*
 *	Copyright (C) 2015  Andre GUENEY													*
 *																						*
 *  This library is free software; you can redistribute it and/or						*
 *  modify it under the terms of the GNU Lesser General Public							*
 *  License as published by the Free Software Foundation; either						*
 *  version 2.1 of the License, or (at your option) any later version.					*
 *																						*
 *  This library is distributed in the hope that it will be useful,						*
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of						*
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU					*
 *  Lesser General Public License for more details.										*
 *																						*
 *  You should have received a copy of the GNU Lesser General Public					*
 *  License along with this library; if not, write to the Free Software					*
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA		*
 *  																					*
 ****************************************************************************************/

package org.gag.production;

/**
 * This object is used to store the Selector variable
 * @author Andre GUENEY
 *
 * @param <T>
 */

public class Selector<T> implements Cloneable{

	protected T selectorName;

	public Selector(){
	}
	
	public Selector(T selector){
		this.selectorName = (T) selector;
	}
	
	public T getSelectorName(){
		return (T) this.selectorName;
	}

	public void setSelectorName(T selector){
		this.selectorName = (T) selector;
	}
	
	public String toString(){
		return this.selectorName.toString();
	}
	
	public Selector<?> clone(){
		Selector<?> clonedSelector = new Selector<T>(this.getSelectorName());
		return clonedSelector;
	}
	
	//*/
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 33;
		int result = 1;
		result = prime * result + this.selectorName.hashCode();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Selector)) {
			return false;
		}
		Selector<?> other = (Selector<?>) obj;
		if (!this.selectorName.equals(other.selectorName)) {
			return false;
		}
		return true;
	}
	
	//*/
	
}
