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

package org.gag.term;

/**
 * This class store the variable used in the term
 * @author Andre GUENEY
 *
 */
public class Variable extends Term {
	
	public Variable(String name) {
		super(name);
	}
	
	/**
	 * 
	 */
	public boolean isVariable(){
		return true;
	}

	/**
	 * 
	 */
	@Override
	public Substitution match(Term other) {
		Substitution res = new Substitution();
		System.out.println("This : "+this+" / Other : "+other);
		if (!this.equals(other)){
			res.setSubstitution(this, other.clone());
		}
		return res;
	}
	
	/**
	 * Return a cloned version of this Variable
	 */
	public Variable clone(){
		return new Variable(this.name);
	}
	
	/**
	 * 
	 */
	public String toString(){
		return this.name;
	}

}
