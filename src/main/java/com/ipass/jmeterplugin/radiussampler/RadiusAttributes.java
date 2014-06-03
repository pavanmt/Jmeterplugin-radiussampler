package com.ipass.jmeterplugin.radiussampler;

import java.io.Serializable;
import org.apache.jmeter.testelement.AbstractTestElement;


public class RadiusAttributes  extends AbstractTestElement implements Serializable
{
	private static final long serialVersionUID = -4567730564529309125L;
	private static final String NAME = "Packet.name";
	private static final String VALUE = "Packet.value";

	public RadiusAttributes()
	{
		setName("");
		setValue("");
	}

	public RadiusAttributes(String name, String value)
	{
		setName(name);
		setValue(value);
	}

	public String getName()
	{
		return getPropertyAsString("Packet.name");
	}

	public void setName(String name)
	{
		if (name != null)
			setProperty("Packet.name", name.trim());
	}

	public String getValue()
	{
		return getPropertyAsString("Packet.value");
	}

	public void setValue(String value)
	{
		if (value != null)
			setProperty("Packet.value", value.trim());
	}

	public String toString()
	{
		return getName() + "\t" + getValue();
	}

	public int hashCode()
	{
		return getPropertyAsString("Packet.name").hashCode();
	}

	public boolean equals(Object o)
	{
		return getPropertyAsString("Packet.name").equals(
				((RadiusAttributes)o).getPropertyAsString("Packet.name"));
	}
}
