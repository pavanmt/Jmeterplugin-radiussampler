package com.ipass.jmeterplugin.radiussampler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.util.JOrphanUtils;

public class RadiusAttributesManager extends ConfigTestElement implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -45147730736344693L;

	public static final String ATTRIBUTES = "RadiusAttributesManager.headers";// $NON-NLS-1$

	private final static String[] COLUMN_RESOURCE_NAMES = { "Attribute Name", // $NON-NLS-1$
			"Attribute Value" // $NON-NLS-1$
	};

	private final static int COLUMN_COUNT = COLUMN_RESOURCE_NAMES.length;
	
	

	public RadiusAttributesManager()
	{
		setProperty(new CollectionProperty(ATTRIBUTES, new ArrayList<Object>()));
	}

	@Override
	public void clear()
	{
		super.clear();

		setProperty(new CollectionProperty(ATTRIBUTES, new ArrayList<Object>()));
	}

	public CollectionProperty getAttributes()
	{

		return (CollectionProperty) getProperty(ATTRIBUTES);
	}

	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public String getColumnName(int column)
	{
		return COLUMN_RESOURCE_NAMES[column];
	}

	public Class<? extends String> getColumnClass(int column)
	{
		return COLUMN_RESOURCE_NAMES[column].getClass();
	}

	public RadiusAttributes getAttribute(int row)
	{
		return (RadiusAttributes) getAttributes().get(row).getObjectValue();
	}

	/**
	 * Add a header.
	 */
	public void add(RadiusAttributes h)
	{
		getAttributes().addItem(h);
	}

	/**
	 * Add an empty header.
	 */
	public void add()
	{
		getAttributes().addItem(new RadiusAttributes());
	}

	/**
	 * Remove a header.
	 */
	public void remove(int index)
	{
		getAttributes().remove(index);
	}

	/**
	 * Return the number of headers.
	 */
	public int size()
	{
		return getAttributes().size();
	}

	/**
	 * Return the header at index i.
	 */
	public RadiusAttributes get(int i)
	{
		return (RadiusAttributes) getAttributes().get(i).getObjectValue();
	}

	/**
	 * Add header data from a file.
	 */
	public void addFile(String headerFile) throws IOException
	{

		File file = new File(headerFile);
		if (!file.isAbsolute())
		{
			file = new File(System.getProperty("user.dir")// $NON-NLS-1$
					+ File.separator + headerFile);
		}
		if (!file.canRead())
		{
			throw new IOException("The file you specified cannot be read.");
		}

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file)); // TODO Charset ?
			String line;
			while ((line = reader.readLine()) != null)
			{
				try
				{
					if (line.startsWith("#") || line.trim().length() == 0)
					{// $NON-NLS-1$
						continue;
					}
					String[] st = JOrphanUtils.split(line, "\t", " ");// $NON-NLS-1$
																		// $NON-NLS-2$
					int name = 0;
					int value = 1;
					RadiusAttributes arAttribute = new RadiusAttributes(st[name], st[value]);
					getAttributes().addItem(arAttribute);
				} catch (Exception e)
				{
					throw new IOException("Error parsing header line\n\t'"
							+ line + "'\n\t" + e);
				}
			}
		} finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * Save the header data to a file.
	 */
	public void save(String headFile) throws IOException
	{
		File file = new File(headFile);
		if (!file.isAbsolute())
		{
			file = new File(System.getProperty("user.dir")// $NON-NLS-1$
					+ File.separator + headFile);
		}
		PrintWriter writer = new PrintWriter(new FileWriter(file)); // TODO
																	// Charset ?
		writer.println("# JMeter generated Header file");// $NON-NLS-1$
		final CollectionProperty hdrs = getAttributes();
		for (int i = 0; i < hdrs.size(); i++)
		{
			final JMeterProperty hdr = hdrs.get(i);
			RadiusAttributes arAttribute = (RadiusAttributes) hdr.getObjectValue();
			writer.println(arAttribute.toString());
		}
		writer.flush();
		writer.close();
	}

}
