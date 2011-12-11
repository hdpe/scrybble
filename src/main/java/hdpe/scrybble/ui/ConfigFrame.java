package hdpe.scrybble.ui;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.dict.SOWPODSDictionary;
import hdpe.scrybble.dict.TWL06Dictionary;
import hdpe.scrybble.game.Dictionary;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * @author Ryan Pickett
 *
 */
class ConfigFrame extends JFrame {

	private JComboBox dictionaryField;
	private JTextField definitionsUrlField;
	
	ConfigFrame(final GameFrame gameFrame) {
		super("Options");
		
		JTabbedPane tabs = new JTabbedPane();

		final JPanel dictionaryPanel = new JPanel() {{
			add(new JLabel("Dictionary"));			
			dictionaryField = new JComboBox(new DictionaryFieldModel());			
			add(dictionaryField);
		}};		

		final JPanel definitionsPanel = new JPanel() {{
			setLayout(new SpringLayout());
			
			definitionsUrlField = new JTextField();
			add(new JLabel("URL"));
			add(definitionsUrlField);
			
			UIUtil.makeSprings(this, 1, 2);
		}};

		tabs.addTab("Dictionary", new JPanel() {{ 
			setLayout(new BorderLayout()); 
			add(dictionaryPanel, BorderLayout.NORTH);
		}});
		tabs.addTab("Definitions", new JPanel() {{ 
			setLayout(new BorderLayout());
			add(definitionsPanel, BorderLayout.NORTH); 
		}});

		
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				gameFrame.fireConfigurationChange(getConfiguration());
				ConfigFrame.this.dispose();
			}
		});
		
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				ConfigFrame.this.dispose();
			}
		});
		
		Container c = getContentPane();
		
		c.setLayout(new BorderLayout());
		
		c.add(tabs, BorderLayout.CENTER);
		c.add(new JPanel() {{
			add(okButton);
			add(cancelButton);
		}}, BorderLayout.SOUTH);
		
		pack();
	}
	
	void setConfiguration(Configuration config) {
		setSelectedDictionary(config.getDictionary());
		setDefinitionsUrl(config.getDefinitionsUrl());
	}
	
	Configuration getConfiguration() {
		Configuration c = Configuration.getDefaultConfiguration();
		try {
			c.setDictionary(((DictionaryFieldModelDictionary)dictionaryField
					.getSelectedItem()).clazz.newInstance());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	protected String getDefinitionsUrl() {
		return definitionsUrlField.getText().trim();
	}
	
	protected void setSelectedDictionary(Dictionary dictionary) {
		for (int i = 0, n = dictionaryField.getItemCount(); i < n; i ++) {
			DictionaryFieldModelDictionary d = (DictionaryFieldModelDictionary)
					dictionaryField.getItemAt(i);
			if (d.clazz.equals(dictionary.getClass())) {
				dictionaryField.setSelectedItem(d);
			}
		}
	}
	
	protected void setDefinitionsUrl(String url) {
		definitionsUrlField.setText(url);
	}
	
	private static class DictionaryFieldModel extends DefaultComboBoxModel {
		DictionaryFieldModel() {
			addElement(new DictionaryFieldModelDictionary("TWL06 (178,691 words)", 
					TWL06Dictionary.class));
			addElement(new DictionaryFieldModelDictionary("SOWPODS (267,750 words)", 
					SOWPODSDictionary.class));
		}
	}
	
	private static class DictionaryFieldModelDictionary {
		private String label;
		private Class<? extends Dictionary> clazz;
		
		DictionaryFieldModelDictionary(String label, 
				Class<? extends Dictionary> clazz) {
			this.label = label;
			this.clazz = clazz;
		}
		
		public String toString() {
			return label;
		}
	}
}
