/* **************************************************************************************
 *												*
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
 * This class contains the sort part
 * @author Andre GUENEY
 *
 * @param <T>
 */
public class Sort<T> {

	protected T sortName;
	
	public Sort(){	
	}
	
	public Sort(T sortName){
		this.sortName = (T) sortName;
	}
	
	public T getSortName(){
		return (T) this.sortName;
	}
	
	public void setSortName(T sortName){
		this.sortName = (T) sortName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 35;
		int result = 1;
		result = prime * result + this.sortName.hashCode();
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
		if (!(obj instanceof Sort)) {
			return false;
		}
		Sort<?> other = (Sort<?>) obj;
		if (!this.sortName.equals(other.sortName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	public String toString(){
		try {
			return this.sortName.toString();
		} catch (NullPointerException e) {
			return "";
		}
	}
	
}
