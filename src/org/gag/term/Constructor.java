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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Set;

import org.gag.production.Selector;

/**
 * This class is the constructor part of a term
 * @author Andre GUENEY
 *
 */

public class Constructor extends Term{
	
	Map<Selector<?>, Term> children=null;
		
	public Constructor(String name){
		super(name);
		this.children = new HashMap<Selector<?>, Term>();
	}
	
	/**
	 * 
	 * @param name
	 * @param mapChild
	 */
	public Constructor(String name, Map<Selector<?>, Term> mapChild){
		super(name);
		this.children = mapChild;
	}
	
	/**
	 * 
	 */
	public Map<Selector<?>, Term> getChilds(){
		return this.children;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		int i = 0;
		String res = "";
		res += this.name + "(";
		if(!this.children.isEmpty()){
			for(Entry<Selector<?>,Term> childEntry : this.children.entrySet()){
				if(i==0) res+= childEntry.getKey()+"="+childEntry.getValue();
				else res+= ", "+childEntry.getKey()+"="+childEntry.getValue();
				i=1;
			}
		} else res +="Empty children";
		res += ")";
		return res;
	} 
	
	/**
	 * Add a Term to the Constructor (Constante, Constructor or Variable)
	 * Return a boolean, true if the adding operation has been well executed or false if not
	 */
	public boolean add(Selector<?> selector,Term term){
		
		if(this.children == null) this.children =  new HashMap<Selector<?>, Term>();
		this.children.put(selector, term.clone());
		return true;
	}
	
	/**
	 * Replace a Term in the Constructor (Constante, Constructor or Variable)
	 * Return the suppressed Term as the result of the operation
	 */
	public Term replace(Selector<?> selector,Term term){
		Term removedTerm = null;
		if(this.children.containsKey(selector)){
			removedTerm = this.children.get(selector);
			this.children.put(selector, term.clone());
		}
		return removedTerm;
	}
	
	/**
	 * Merging two term.
	 * The inner term of this object and an outside term
	 */
	@Override
	public Substitution match(Term other) {
		Substitution res = new Substitution();
		if (!this.equals(other))
			res.fail();
		else {
			/* Both constructors are identical -- same name, same arity -- */
			Constructor oth = (Constructor) other;
			
			for(Selector<?> key : this.children.keySet()){
				Term this_t = this.children.get(key), other_t= oth.children.get(key);
				Substitution newSub;
				newSub = this_t.match(other_t);
				if(newSub.isFailed()){
					res.fail();
					break;
				}
				else
					res.merge(newSub);
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.children.size();
		result = prime * result + this.name.hashCode();
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
		if (!(obj instanceof Constructor)) {
			return false;
		}
		Constructor other = (Constructor) obj;
		if (this.children.size() != other.children.size()) {
			return false;
		}
		if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Apply the substitution on the current Term and, by recursion, apply it on each children
	 */
	public boolean applySubst(Substitution subst){
		
		Set<Entry<Selector<?>, Term>> entries = this.children.entrySet();
		boolean mod=false;
		for(Entry<Selector<?>, Term> childMapEntry : entries){
			if(childMapEntry.getValue().isVariable() && subst.getSubstitution().containsKey(childMapEntry.getValue())){
				this.replace(childMapEntry.getKey(),subst.getSubstitution().get(childMapEntry.getValue()));
				mod=true;
			}
			else {	
				childMapEntry.getValue().applySubst(subst);
			}
		}
		return mod;
	}
	
	/**
	 * Return a cloned version of this term
	 */
	public Term clone(){
		Constructor copy = new Constructor(this.name);
		Set<Entry<Selector<?>, Term>> entries = this.children.entrySet();
		for(Entry<Selector<?>,Term> childMapEntry : entries){
			copy.add(childMapEntry.getKey(),childMapEntry.getValue().clone());
		}
		return copy;
	}
	
}
