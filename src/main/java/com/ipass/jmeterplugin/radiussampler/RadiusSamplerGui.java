package com.ipass.jmeterplugin.radiussampler;

import java.awt.BorderLayout;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class RadiusSamplerGui extends AbstractSamplerGui
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4301573282262663481L;

	RadiusConfigGUI configGUI;

	public RadiusSamplerGui()
	{
		init();
	}



	public TestElement createTestElement()
	{
	
		RadiusSampler sampler = new RadiusSampler();
		modifyTestElement(sampler);
		return sampler;
	}



	public String getStaticLabel()
	{
		return "Radius Protocol Sampler";
	}



	public String getLabelResource()
	{
		return getClass().getSimpleName();
	}



	public void modifyTestElement(TestElement sampler)
	{
		sampler.clear();
		sampler.addTestElement(this.configGUI.createTestElement());
		configureTestElement(sampler);
	}


	public void configure(TestElement element)
	{
		super.configure(element);
		this.configGUI.configure(element);
	}

	private void init()
	{
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());

		this.configGUI = new RadiusConfigGUI(true);

		add(this.configGUI, "Center");
	}

}
