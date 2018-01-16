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

package org.gag.xml;

/**
 * This class convert the String format of <T> from the xml file in the correct type
 * 
 * @author Andre GUENEY
 *
 * @param <T>
 */
public class TypeConverter<T> {

	T value;
	
	@SuppressWarnings("unchecked")
	public T convertType(String type, String value){

		if(type.contains("Boolean")){
			this.value = (T) Boolean.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Character")){
			this.value = (T) value;
			return this.value;
		}
		
		if(type.contains("String")){
			this.value = (T) value;
			return this.value;
		}
		
		if(type.contains("Byte")){
			this.value = (T) Byte.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Short")){
			this.value = (T) Short.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Integer")){
			this.value = (T) Integer.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Long")){
			this.value = (T) Long.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Float")){
			this.value = (T) Float.valueOf(value);
			return this.value;
		}
		
		if(type.contains("Double")){
			this.value = (T) Double.valueOf(value);
			return this.value;
		}

		return null;
		
	}
	
}
