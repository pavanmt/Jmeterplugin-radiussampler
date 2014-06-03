package com.ipass.jmeterplugin.radiussampler;

import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.soap.providers.com.Log;
import org.htmlparser.Remark;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;


public class AddAttributes extends RadiusAttribute{


	public AccessRequest addAuthRadiusAttribute(AccessRequest accessRequest,CollectionProperty collectionProperty){

		int size = collectionProperty.size();
		//collectionProperty=removeAttributes(collectionProperty);
		for (int row = 0; row < size; row++)
		{

			JMeterProperty rowProperty = collectionProperty.get(row);
			RadiusAttributes arAttribute = (RadiusAttributes) rowProperty.getObjectValue();

			String name = arAttribute.getName();
			String value = arAttribute.getValue();
			if(name!=null && value!=null && !checkPaddedAttribute(name))
				accessRequest.addAttribute(name, value);

		}

		return accessRequest;
	}

	public static void main(String[] args) {
		CollectionProperty collectionProperty = new CollectionProperty();
		collectionProperty.addItem("User-Name\tpavan");
		collectionProperty.addItem("User-Password\tpavan");

		System.out.println(collectionProperty);
		System.out.println(new AddAttributes().removeAttributes(collectionProperty));
	}

	public CollectionProperty removeAttributes(CollectionProperty collectionProperty){
		try{
			int size=collectionProperty.size();

			for(int i=0;i<size;i++)
			{
				if(collectionProperty.get(i).toString().toLowerCase().startsWith("user-name"))
					collectionProperty.remove(i);

				if(collectionProperty.get(i).toString().toLowerCase().startsWith("user-password"))
					collectionProperty.remove(i);

			}
		}catch
		(Exception e){

		}

		return collectionProperty;
	}
	
	public boolean checkPaddedAttribute(String name){
		try{
			
			if(name.toLowerCase().startsWith("user-name")||name.toLowerCase().startsWith("user-password"))
				return true;
			
			
		}catch
		(Exception e){

		}

		return false;
	}

	public AccountingRequest addAcctRadiusAttribute(AccountingRequest acctRequest,CollectionProperty collectionProperty){

		int size = collectionProperty.size();
		//collectionProperty=removeAttributes(collectionProperty);
		for (int row = 0; row < size; row++)
		{
			JMeterProperty rowProperty = collectionProperty.get(row);
			RadiusAttributes arAttribute = (RadiusAttributes) rowProperty.getObjectValue();

			String name = arAttribute.getName();
			String value = arAttribute.getValue();
			if(name!=null && value!=null && !checkPaddedAttribute(name)){
				acctRequest.addAttribute(name, value);
			}else{
				System.out.println("name and val is empty - "+name+"val"+value);
			}
		}

		return acctRequest;
	}


	public String getRequiredAttribute(CollectionProperty collectionProperty,String attributeName){

		int size = collectionProperty.size();

		for (int row = 0; row < size; row++)
		{
			JMeterProperty rowProperty = collectionProperty.get(row);
			RadiusAttributes arAttribute = (RadiusAttributes) rowProperty.getObjectValue();

			String name = arAttribute.getName();
			String value = arAttribute.getValue();

			if(name!=null && name.equalsIgnoreCase(attributeName)){
				return value;
			}

		}

		return null;

	}

}
