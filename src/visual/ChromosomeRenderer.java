package visual;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.border.Border;

import modelo.Chromosome;



public class ChromosomeRenderer extends DefaultListCellRenderer {
	public ChromosomeRenderer() {
	}
	private Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY); // Borda inferior para a divisão

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Chromosome) {
            Chromosome chromosome = (Chromosome) value;
            StringBuilder sb = new StringBuilder();

            DecimalFormat df = new DecimalFormat("#.##");

            sb.append("<html>");
            sb.append("<b>Índice:</b> ").append(index+1).append("<br>");
            sb.append("<b>Classificação RB:</b> ").append(chromosome.getClassification()).append("<br>");
            sb.append("<b>Natureza:</b> ").append(chromosome.getNature()).append("<br>");
            sb.append("<b>Braço Maior:</b> ").append(df.format(chromosome.getLongerArmLength())).append("<br>");
            sb.append("<b>Braço Menor:</b> ").append(df.format(chromosome.getShorterArmLength())).append("<br>");
            sb.append("<b>Razão dos Braços:</b> ").append(df.format(chromosome.getArmRatio())).append("<br>");
            sb.append("<b>Índice Centromérico:</b> ").append(df.format(chromosome.getCentromericIndex())).append("<br>");
            sb.append("<b>Comprimento Total:</b> ").append(df.format(chromosome.getTotalLength())).append("<br>");
            sb.append("</html>");

            setText(sb.toString());
        }
        
        if (index < list.getModel().getSize() - 1) {
            setBorder(BorderFactory.createCompoundBorder(getBorder(), border));
        } else {
            setBorder(getBorder()); // Remove a borda do último item
        }
        
        return this;
    }
}