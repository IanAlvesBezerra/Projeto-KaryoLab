package controle;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import modelo.Band;
import modelo.Chromosome;
import modelo.Position;
import visual.Dialog;
import visual.Frame;
import visual.PanelCariotipagem;
import visual.PanelInfo;
import visual.PanelRelatorio;
import visual.Zoom;

public class ControladorPanelCariotipagem implements ActionListener {

	Frame frame;
	PanelCariotipagem panelCariotipagem;
	String imagePath;
	boolean isMarkingCentriolo = false;
	int isMarkingArms = 0; //0 = false; 1 = primeiro braço; 2 = segundo braço
	boolean isMarkingBands = false;
	Position centriolo;
	Position node;
	MouseAdapter mouseListener;
	String savePath;
	String speciesName;
	Zoom zoomDialog;
	Color color;
	int ploidy;
	
	public ControladorPanelCariotipagem(Frame frame, PanelCariotipagem panelCariotipagem, boolean load){
		this.frame = frame;
		this.panelCariotipagem = panelCariotipagem;
		addEventos();
		validateButtons();
		if(load) loadKaryotyping();
		else newKaryotype(false);
//		help();
	}
	
	private void addEventos() {
		panelCariotipagem.getButtonUploadFotograma().addActionListener(this);
		panelCariotipagem.getButtonMarcar().addActionListener(this);
		panelCariotipagem.getButtonCorrigir().addActionListener(this);
		panelCariotipagem.getButtonExpandir().addActionListener(this);
		panelCariotipagem.getButtonDiminuir().addActionListener(this);
		panelCariotipagem.getButtonExcluirSelecionado().addActionListener(this);
		panelCariotipagem.getButtonNovaCariotipagem().addActionListener(this);
		panelCariotipagem.getButtonSalvar().addActionListener(this);
		panelCariotipagem.getButtonCarregar().addActionListener(this);
		panelCariotipagem.getButtonGerarRelatorio().addActionListener(this);
		panelCariotipagem.getButtonAlternarNatureza().addActionListener(this);
		panelCariotipagem.getButtonZoom().addActionListener(this);
		panelCariotipagem.getButtonMarcarBanda().addActionListener(this);
		panelCariotipagem.getButtonLinhasdeGrade().addActionListener(this);
		panelCariotipagem.getButtonSalvarComo().addActionListener(this);
		panelCariotipagem.getButtonFatorDeGrade().addActionListener(this);
		
		
		panelCariotipagem.getListChromosomes().addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) { // Evita chamadas duplas
	        	// Permite a utilização dos botões de excluir e alternar se houver algum cromossomo selecionado
	            if (panelCariotipagem.getListChromosomes().getSelectedValue() != null) {
	                panelCariotipagem.getButtonExcluirSelecionado().setVisible(true); 
	                panelCariotipagem.getButtonExcluirSelecionado().setEnabled(true);
	                panelCariotipagem.getButtonAlternarNatureza().setVisible(true);
	                panelCariotipagem.getButtonAlternarNatureza().setEnabled(true);
	            } else {
	                panelCariotipagem.getButtonExcluirSelecionado().setVisible(false);
	                panelCariotipagem.getButtonExcluirSelecionado().setEnabled(false);
	                panelCariotipagem.getButtonAlternarNatureza().setVisible(false);
	                panelCariotipagem.getButtonAlternarNatureza().setEnabled(false);
	            }
	            
	            //--- Atualiza o botão Alternar Natureza
	    		if(panelCariotipagem.getListChromosomes().getSelectedValue() != null) {
	    			Chromosome selectedChromosome = panelCariotipagem.getListChromosomes().getSelectedValue();
	    	        panelCariotipagem.getPanelViewFotograma().setSelectedChromosome(selectedChromosome);

	    			if(selectedChromosome.getNature() == "Sexual") {
	    				panelCariotipagem.getButtonAlternarNatureza().setText("<html>Definir<br>como Autossômico</html>");
	    			} else {
	    				panelCariotipagem.getButtonAlternarNatureza().setText("<html>Definir<br>como Sexual</html>");
	    			}
	    		} else {
	    			panelCariotipagem.getPanelViewFotograma().setSelectedChromosome(null);
	    		}
	        }
	    });
		
		panelCariotipagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!estaDentroDaLista(e.getX(), e.getY())) {
                	panelCariotipagem.getListChromosomes().clearSelection();
                }
            }
        });
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelCariotipagem.getButtonUploadFotograma()) {
			uploadImage();
		} else if (e.getSource() == panelCariotipagem.getButtonMarcar()) {
			if (isMarkingCentriolo || isMarkingArms == 1) {
	        	deactivateCentrioloMarking();
	        	activateArmMarking();
	        }
	        else if(isMarkingArms == 2) {
	        	deactivateArmMarking();
	        }
	        else {
	        	activateCentrioloMarking();
	        }
	    } else if(e.getSource() == panelCariotipagem.getButtonCorrigir()) {
	    	fix();
	    } else if(e.getSource() == panelCariotipagem.getButtonExpandir()) {
	    	panelCariotipagem.getPanelViewFotograma().expandMarkings();
	    } else if(e.getSource() == panelCariotipagem.getButtonDiminuir()) {
	    	panelCariotipagem.getPanelViewFotograma().decreaseMarkings();
	    } else if(e.getSource() == panelCariotipagem.getButtonExcluirSelecionado()) {
	        deleteSelectedChromosome();
	    } else if(e.getSource() == panelCariotipagem.getButtonNovaCariotipagem()) {
	    	newKaryotype(false);
	    } else if(e.getSource() == panelCariotipagem.getButtonSalvar()) {
	    	saveChanges();
	    } else if(e.getSource() == panelCariotipagem.getButtonCarregar()) {
	    	loadKaryotyping();
	    } else if(e.getSource() == panelCariotipagem.getButtonGerarRelatorio()) {
	    	generateReport();
	    } else if(e.getSource() == panelCariotipagem.getButtonAlternarNatureza()) {
	    	alternateSelectedChromosome();
	    } else if(e.getSource() == panelCariotipagem.getButtonZoom()) {
	    	zoom();
	    } else if(e.getSource() == panelCariotipagem.getButtonMarcarBanda()) {
	    	if(!isMarkingBands) {
	    		activateBandMarking();
	    	} else {
	    		deactivateBandMarking();
	    	}
	    } else if(e.getSource() == panelCariotipagem.getButtonLinhasdeGrade()) {
	    	alternateGridLines();
	    } else if(e.getSource() == panelCariotipagem.getButtonSalvarComo()) {
	    	saveAs();
	    } else if(e.getSource() == panelCariotipagem.getButtonFatorDeGrade()) {
	    	changeGridFactor();
	    }

		validateButtons();
		panelCariotipagem.getListChromosomes().clearSelection();
		panelCariotipagem.revalidate();
		panelCariotipagem.repaint();
	}
	
// -------------------------- VALIDACOES -------------------------

	public void validateButtons() {
		//--- valida o botão de marcar
		if(imagePath == null) {
			panelCariotipagem.getButtonMarcar().setEnabled(false);
		}
		else {
			panelCariotipagem.getButtonMarcar().setEnabled(true);
			// somente libera a marcação do primeiro braço se um centriolo for marcado
			if(panelCariotipagem.getPanelViewFotograma().getCentrioloPreview() == null && panelCariotipagem.getButtonMarcar().getText() == "Marcar braços") {
				panelCariotipagem.getButtonMarcar().setEnabled(false);
			}
			else {
				panelCariotipagem.getButtonMarcar().setEnabled(true);
				// somente libera a marcação do próximo braço quando ao menos um nó for dado e não estiver marcando bandas
				if(isMarkingBands || (panelCariotipagem.getButtonMarcar().getText() == "Marcar segundo braço" || panelCariotipagem.getButtonMarcar().getText() == "Parar marcação") && panelCariotipagem.getPanelViewFotograma().getArrayNodes(isMarkingArms).size() == 0) {
					panelCariotipagem.getButtonMarcar().setEnabled(false);
				}
				else {
					panelCariotipagem.getButtonMarcar().setEnabled(true);
				}
			}
		}
		
		//--- valida o botão de corrigir
		// se estiver marcando braços ou se o tamanho do array de nodes for maior que 0
		if(isMarkingArms == 0 || panelCariotipagem.getPanelViewFotograma().getArrayNodes(isMarkingArms).size() <= 0) {
			panelCariotipagem.getButtonCorrigir().setEnabled(false);
		}
		else {
			panelCariotipagem.getButtonCorrigir().setEnabled(true);
		}
		
		//--- valida os botões de expandir e diminuir
		if(panelCariotipagem.getPanelViewFotograma().getRadius() <= 2) {
			panelCariotipagem.getButtonDiminuir().setEnabled(false);
			panelCariotipagem.getButtonExpandir().setEnabled(true);
		}
		else if(panelCariotipagem.getPanelViewFotograma().getRadius() >= 10) {
			panelCariotipagem.getButtonExpandir().setEnabled(false);
			panelCariotipagem.getButtonDiminuir().setEnabled(true);
		}
		else {
			panelCariotipagem.getButtonDiminuir().setEnabled(true);
			panelCariotipagem.getButtonExpandir().setEnabled(true);
		}
		
		//--- valida o botão de marcar banda
		if(isMarkingArms != 0) {
            panelCariotipagem.getButtonMarcarBanda().setEnabled(true);
            if(isMarkingBands && panelCariotipagem.getPanelViewFotograma().getArrayBandPositions().size() < 2) {
    			panelCariotipagem.getButtonMarcarBanda().setEnabled(false);
    		} else {
    			panelCariotipagem.getButtonMarcarBanda().setEnabled(true);
    		}
		} else {
            panelCariotipagem.getButtonMarcarBanda().setEnabled(false);
		}
		
		//--- valida o botão do fator de grade
		if(panelCariotipagem.getPanelViewFotograma().isShowingGridLines()) {
			panelCariotipagem.getButtonFatorDeGrade().setVisible(true);
		} else {
			panelCariotipagem.getButtonFatorDeGrade().setVisible(false);
		}
	}
	
	
	
	private boolean estaDentroDaLista(int x, int y) {
        int listaX = panelCariotipagem.getListChromosomes().getLocationOnScreen().x;
        int listaY = panelCariotipagem.getListChromosomes().getLocationOnScreen().y;
        int listaLargura = panelCariotipagem.getListChromosomes().getWidth();
        int listaAltura = panelCariotipagem.getListChromosomes().getHeight();

        return x >= listaX && x <= listaX + listaLargura && y >= listaY && y <= listaY + listaAltura;
    }
	
// -------------------------- MARCACOES ---------------------------------
	
	private void activateCentrioloMarking() {
		isMarkingCentriolo = true;
		panelCariotipagem.getPanelViewFotograma().setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
		panelCariotipagem.getButtonMarcar().setText("Marcar braços");
		
		mouseListener = new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            centriolo = new Position(e.getX(), e.getY());
	            drawCentriolo(centriolo);
	            validateButtons();
	        }
	    };

	    panelCariotipagem.getPanelViewFotograma().addMouseListener(mouseListener);
	}
	
	private void deactivateCentrioloMarking() {
		isMarkingCentriolo = false;
		panelCariotipagem.getPanelViewFotograma().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		if(centriolo != null) {
			panelCariotipagem.getPanelViewFotograma().fixCentriolo();
		}
		panelCariotipagem.getPanelViewFotograma().removeMouseListener(mouseListener);
	}
	
	private void drawCentriolo(Position centriolo) {
		panelCariotipagem.getPanelViewFotograma().viewCentriolo(centriolo);
	}
	
	
	private void activateArmMarking() {
		isMarkingArms++;
		panelCariotipagem.getPanelViewFotograma().setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
		panelCariotipagem.getButtonMarcar().setText((isMarkingArms == 1) ? "<html>Marcar<br>segundo braço</html>" : "Parar marcação");
		
		mouseListener = new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            node = new Position(e.getX(), e.getY());
	            drawNode(node);
	            if(isMarkingBands) {
	            	panelCariotipagem.getPanelViewFotograma().addNodeToBand(node);
	            }
	            validateButtons();
	        }
	    };

	    panelCariotipagem.getPanelViewFotograma().addMouseListener(mouseListener);
	}
	
	private void deactivateArmMarking() {
		isMarkingArms = 0;
		panelCariotipagem.getPanelViewFotograma().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		panelCariotipagem.getButtonMarcar().setText("<html>Marcar<br>novo centrômero</html>");
		panelCariotipagem.getPanelViewFotograma().addChromosome();
		panelCariotipagem.getPanelViewFotograma().resetArmsPreview();
		
		panelCariotipagem.getPanelViewFotograma().removeMouseListener(mouseListener);
		
		//Atualizando a lista de cromossomos
		panelCariotipagem.getListChromosomes().setModel(getChromosomesData());
	}
	
	private void drawNode(Position node){
		panelCariotipagem.getPanelViewFotograma().addNode(node, isMarkingArms);
	}
	
	private void fix() {
	    Position lastNode = panelCariotipagem.getPanelViewFotograma().getArrayNodes(isMarkingArms).getLast();

	    // Remove a última posição da banda *SE* ela for igual à última posição do nó.
	    if (!panelCariotipagem.getPanelViewFotograma().getArrayBandPositions().isEmpty() &&
	            panelCariotipagem.getPanelViewFotograma().getArrayBandPositions().getLast().equals(lastNode)) {
	        panelCariotipagem.getPanelViewFotograma().getArrayBandPositions().removeLast();
	    }

	    if (!panelCariotipagem.getPanelViewFotograma().getArrayBands().isEmpty()) {
	        Band lastBand = panelCariotipagem.getPanelViewFotograma().getArrayBands().getLast();
	        if (!lastBand.getBandCoordinates().isEmpty() && lastBand.getBandCoordinates().getLast().equals(lastNode)) {
	            lastBand.getBandCoordinates().removeLast();
	            // Remove a banda inteira *SE* ela ficar com menos de 2 nós *APÓS* a remoção do nó.
	            if (lastBand.getBandCoordinates().size() < 2) {
	                panelCariotipagem.getPanelViewFotograma().getArrayBands().removeLast();
	            }
	        }
	    }

	    panelCariotipagem.getPanelViewFotograma().getArrayNodes(isMarkingArms).removeLast();
	    panelCariotipagem.repaint();
	}
	
	private void activateBandMarking() {
		isMarkingBands = true;
		panelCariotipagem.getButtonMarcarBanda().setText("Finalizar Banda");
		color = JColorChooser.showDialog(null,"Selecione uma cor para a banda", Color.RED);
		if(color != null) {
			panelCariotipagem.getPanelViewFotograma().setBandPreviewColor(color);			
		} else {
			isMarkingBands = false;
			panelCariotipagem.getButtonMarcarBanda().setText("Marcar Banda");
			JOptionPane.showMessageDialog(null, "Marcação de banda cancelada", "Banda cancelada", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void deactivateBandMarking() {
	    isMarkingBands = false;
	    panelCariotipagem.getPanelViewFotograma().addBand(isMarkingArms);
	    panelCariotipagem.getButtonMarcarBanda().setText("Marcar Banda");
	    panelCariotipagem.getPanelViewFotograma().clearBandPreview();
	    panelCariotipagem.repaint();
	}
	
// --------------------- UPLOAD DE IMAGEM --------------------------------------

	public void uploadImage() {
	    try {
	        JFileChooser fc = new JFileChooser();
	        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
	        
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "png", "jpg");
	        
	        fc.addChoosableFileFilter(filter);
	        int result = fc.showOpenDialog(null);
	        
	        if (result == JFileChooser.APPROVE_OPTION) {
	            setImage(fc.getSelectedFile());
	        } else {
	            JOptionPane.showMessageDialog(panelCariotipagem, "Erro! Imagem inválida", "Erro!", JOptionPane.WARNING_MESSAGE);
	        }
	    } catch (HeadlessException e) {
	        JOptionPane.showMessageDialog(panelCariotipagem, "Erro!", "Erro!", JOptionPane.WARNING_MESSAGE);
	    }
	}
	
	public void setImage(File file) {
		if(file.exists()) {
			String path = file.getAbsolutePath();
	        ImageIcon imageIcon = resizeImage(path, null);
	        panelCariotipagem.getPanelViewFotograma().setImage(imageIcon.getImage()); // Armazena a imagem no painel
	        imagePath = path;
		}
		else {
			String message = "A imagem  em " + file.getPath() + " não foi encontrada. \nDeseja selecionar outra?";
			int result = JOptionPane.showConfirmDialog(panelCariotipagem, message, "Imagem não encontrada", JOptionPane.WARNING_MESSAGE);
			
			if(result == JOptionPane.YES_OPTION) {
				uploadImage();
			}
		}
		validateButtons();
	}
	
	public ImageIcon resizeImage(String path, byte[] pic) {
		ImageIcon myImage = null;
		if(path != null) {
			myImage = new ImageIcon(path);
		}
		else {
			myImage = new ImageIcon(pic);
		}
		
		int width = panelCariotipagem.getPanelViewFotograma().getLabelFotograma().getWidth();
		int height = panelCariotipagem.getPanelViewFotograma().getLabelFotograma().getHeight();
		
		Image img1 = myImage.getImage();
		Image img2 = img1.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(img2);
		return image;
	}
	
	// ---------------------- GERENCIAMENTO DOS CROMOSSOMOS -----------------------
	
	private void newKaryotype(boolean loading) {
		// se o array de cromossomos não está vazio
		if(!panelCariotipagem.getPanelViewFotograma().getArrayChromosomes().isEmpty()) {
			int response = JOptionPane.showConfirmDialog(
				null,
				"Você deseja salvar as suas alterações?",
				"Salvar alterações?",
				JOptionPane.YES_NO_CANCEL_OPTION
			);
			if(response == JOptionPane.YES_OPTION) {
				saveChanges();
				clearMarkings();
				if(!loading) inputSpeciesInfo();
			}
			else if(response == JOptionPane.NO_OPTION) {
				int confirmation = JOptionPane.showConfirmDialog(
		            null,
		            "Tem certeza de que deseja limpar TODAS as marcações feitas até o momento?"
		            + "\nAlterações não salvas serão perdidas PERMANENTEMENTE",
		            "Confirmação de exclusão",
		            JOptionPane.YES_NO_OPTION
		        );
				if(confirmation == JOptionPane.YES_OPTION) {
					clearMarkings();
					if(!loading) inputSpeciesInfo();
				} else {
					JOptionPane.showMessageDialog(null, "As marcações foram mantidas!", "Cancelado", JOptionPane.WARNING_MESSAGE);
				}
			}
		} else {
			if(!loading) inputSpeciesInfo();
		}
	}
	
	private void inputSpeciesInfo() {
		speciesName = null;
		ploidy = 0;
		while(speciesName == null || ploidy == 0) {
			PanelInfo panelInfo = new PanelInfo();
			Dialog dialog = new Dialog(frame, panelInfo, 400, 400);
			new ControladorPanelInfo(panelInfo, this, dialog);
			dialog.setVisible(true);
		}
	}
	
	public void setPloidy(int ploidy) {
		this.ploidy = ploidy;
	}
	
	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}
	
	
	private void clearMarkings() {
    	isMarkingArms = 0;
		isMarkingCentriolo = false;
		savePath = null;
		panelCariotipagem.getPanelViewFotograma().setImage(null);
		panelCariotipagem.getPanelViewFotograma().clearMarkings();
		panelCariotipagem.getListChromosomes().setModel(new DefaultListModel<Chromosome>());
	}
	
	private DefaultListModel<Chromosome> getChromosomesData(){
		DefaultListModel<Chromosome> chromosomesList = new DefaultListModel<Chromosome>();
		for(Chromosome chromosome : panelCariotipagem.getPanelViewFotograma().getArrayChromosomes()) {
			chromosomesList.addElement(chromosome);
		}
		return chromosomesList;
	}
	
	private void deleteSelectedChromosome() {
		Chromosome selectedChromosome = panelCariotipagem.getListChromosomes().getSelectedValue();
	    if (selectedChromosome != null) {
	    	
	    	int result = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente excluir este cromossomo?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION
	        );

	        if (result == JOptionPane.YES_OPTION) {
	        	DefaultListModel<Chromosome> model = (DefaultListModel<Chromosome>) panelCariotipagem.getListChromosomes().getModel();
		        model.removeElement(selectedChromosome); // Remove o cromossomo selecionado
		        panelCariotipagem.getPanelViewFotograma().getArrayChromosomes().remove(selectedChromosome);
		        panelCariotipagem.getButtonExcluirSelecionado().setVisible(false); // Esconde o botão de exclusão
	        	JOptionPane.showMessageDialog(null, "Cromossomo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, "O Cromossomo não foi excluído", "Exclusão cancelada", JOptionPane.WARNING_MESSAGE);
	        }
	    }
	}
	
	private void alternateSelectedChromosome() {
		Chromosome selectedChromosome = panelCariotipagem.getListChromosomes().getSelectedValue();
		if(selectedChromosome != null) {
			if(countSexuals() >= 2 && selectedChromosome.getNature() == "Autossômico") {
				int result = JOptionPane.showConfirmDialog(
					null,
					"Já há "+countSexuals()+" Cromossomos sexuais"
					+ "\nDeseja definir mais esse cromossomo como sexual",
					"Confirmação",
					JOptionPane.YES_NO_OPTION
				);
				
				if(result == JOptionPane.YES_OPTION) {
					selectedChromosome.alternateNature();
		        	JOptionPane.showMessageDialog(null, "Cromossomo definido como "+selectedChromosome.getNature(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "O Cromossomo não foi alterado", "Operação cancelada", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				selectedChromosome.alternateNature();
	        	JOptionPane.showMessageDialog(null, "Cromossomo definido como "+selectedChromosome.getNature(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private int countSexuals() {
		int sexuals = 0;
		for(Chromosome c : panelCariotipagem.getPanelViewFotograma().getArrayChromosomes()) {
			if(c.getNature() == "Sexual") {
				sexuals++;
			}
		}
		return sexuals;
	}
	
	private void generateReport(){
		PanelRelatorio panelRelatorio = new PanelRelatorio(Chromosome.findHomologousSets(panelCariotipagem.getPanelViewFotograma().getArrayChromosomes(), ploidy), speciesName);
		Dialog dialog = new Dialog(frame, panelRelatorio);
		new ControladorPanelRelatorio(panelRelatorio, ploidy);
		dialog.setVisible(true);
	}
	
	// ------------------------------ PERSISTENCIA ---------------------------------------

	private void saveChanges() {
		// só permite o salvamento se alguma marcação for feita
		if(panelCariotipagem.getPanelViewFotograma().getArrayChromosomes().size() != 0) {
			
			// se o centriolo ou array de preview não estiver vazio
			if(panelCariotipagem.getPanelViewFotograma().getCentrioloPreview() != null || !panelCariotipagem.getPanelViewFotograma().getArrayNodes(1).isEmpty()) {
				JOptionPane.showMessageDialog(
					null,
					"Existe uma marcação em andamento."
					+ "\nTermine ela para que não seja perdida",
					"Terminar marcação do cromossomo",
					JOptionPane.WARNING_MESSAGE
				);
			}
			
			try {
				
				if(savePath == null) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setDialogTitle("Escolha o local para o salvamento");
					fc.setApproveButtonText("Salvar");
					fc.showOpenDialog(panelCariotipagem);
					File selectedFile = fc.getSelectedFile();
					savePath = selectedFile.getPath();
					savePath+="\\"+speciesName+".xml";
					JOptionPane.showMessageDialog(null, "O arquivo foi salvo como "+speciesName+".xml", "Aviso", JOptionPane.INFORMATION_MESSAGE);
				}
					
				Persistence.generateXML(panelCariotipagem.getPanelViewFotograma().getArrayChromosomes(), savePath, imagePath, ploidy);
				JOptionPane.showMessageDialog(null, "Alterações salvas com sucesso!", "Dados guardados", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (HeadlessException e) {
		        JOptionPane.showMessageDialog(panelCariotipagem, "Erro!", "Erro!", JOptionPane.WARNING_MESSAGE);
		    } catch (NullPointerException e) {
		    	JOptionPane.showMessageDialog(panelCariotipagem, "O arquivo não foi salvo!", "Cancelado!", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Não é possível salvar sem nenhuma marcação feita", "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void saveAs() {
		// só permite o salvamento se alguma marcação for feita
		if(panelCariotipagem.getPanelViewFotograma().getArrayChromosomes().size() != 0) {
			
			// se o centriolo ou array de preview não estiver vazio
			if(panelCariotipagem.getPanelViewFotograma().getCentrioloPreview() != null || !panelCariotipagem.getPanelViewFotograma().getArrayNodes(1).isEmpty()) {
				JOptionPane.showMessageDialog(
					null,
					"Existe uma marcação em andamento."
					+ "\nTermine ela para que não seja perdida",
					"Terminar marcação do cromossomo",
					JOptionPane.WARNING_MESSAGE
				);
			}
			
			try {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Escolha o local para o salvamento");
				fc.setApproveButtonText("Salvar");
				fc.showOpenDialog(panelCariotipagem);
				File selectedFile = fc.getSelectedFile();
				String tempSavePath = selectedFile.getPath();
				tempSavePath+="\\"+speciesName+".xml";
				JOptionPane.showMessageDialog(null, "O arquivo foi salvo como "+speciesName+".xml", "Aviso", JOptionPane.INFORMATION_MESSAGE);
					
				Persistence.generateXML(panelCariotipagem.getPanelViewFotograma().getArrayChromosomes(), tempSavePath, imagePath, ploidy);
				JOptionPane.showMessageDialog(null, "Alterações salvas com sucesso!", "Dados guardados", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (HeadlessException e) {
		        JOptionPane.showMessageDialog(panelCariotipagem, "Erro!", "Erro!", JOptionPane.WARNING_MESSAGE);
		    } catch (NullPointerException e) {
		    	JOptionPane.showMessageDialog(panelCariotipagem, "O arquivo não foi salvo!", "Cancelado!", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Não é possível salvar sem nenhuma marcação feita", "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void loadKaryotyping() {
		newKaryotype(true);
		try {
			
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setDialogTitle("Escolha o local para o salvamento");
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", "xml");
			fc.addChoosableFileFilter(filter);
	        int result = fc.showOpenDialog(panelCariotipagem);
	        
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fc.getSelectedFile();
	            savePath = selectedFile.getPath();
	            // pega o nome do arquivo e remova a extensão
	            speciesName = (selectedFile.getName()).substring(0, selectedFile.getName().lastIndexOf("."));
	            
	            String fotograma = Persistence.readXML(savePath, panelCariotipagem.getPanelViewFotograma().getArrayChromosomes(), this);
	            
	            if(fotograma != null) {
	            	setImage(new File(fotograma));
		            
		            //Atualizando a lista de cromossomos
		    		panelCariotipagem.getListChromosomes().setModel(getChromosomesData());
	            } else {
	            	newKaryotype(false);
	            }
	        
	        } else {
	            JOptionPane.showMessageDialog(panelCariotipagem, "Erro! Arquivo inválido", "Erro!", JOptionPane.WARNING_MESSAGE);
	            newKaryotype(false);
	        }
		
		} catch (HeadlessException e) {
	        JOptionPane.showMessageDialog(panelCariotipagem, "Erro!", "Erro!", JOptionPane.WARNING_MESSAGE);
	    }
	}
	
// -------------------------------- AJUDA ----------------------------------------	
	
	public void help() {
		JOptionPane.showMessageDialog(null, "Lembre-se de marcar os cromossomos de par em par!", "Lembrete!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void zoom() {
		if (zoomDialog == null) {
            try {
                zoomDialog = new Zoom(frame);
                zoomDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        zoomDialog.stop();
                        zoomDialog.dispose();
                        zoomDialog = null;
                    }
                });

            } catch (AWTException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao iniciar o zoom: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return; // Sai do método se houver um erro
            }
        }

        zoomDialog.setVisible(true);
        zoomDialog.start(); 
	}
	
	private void alternateGridLines() {
		if(panelCariotipagem.getPanelViewFotograma().isShowingGridLines()) {
			panelCariotipagem.getPanelViewFotograma().setShowingGridLines(false);
			panelCariotipagem.getButtonLinhasdeGrade().setText("Mostrar Grade");
		} else {
			panelCariotipagem.getPanelViewFotograma().setShowingGridLines(true);
			panelCariotipagem.getButtonLinhasdeGrade().setText("Esconder Grade");
		}
	}
	
	private void changeGridFactor() {
		if(panelCariotipagem.getPanelViewFotograma().getGridScale() == 48) {
			panelCariotipagem.getPanelViewFotograma().setGridScale(24);
			panelCariotipagem.getButtonFatorDeGrade().setText("2x");
		} else if(panelCariotipagem.getPanelViewFotograma().getGridScale() == 24) {
			panelCariotipagem.getPanelViewFotograma().setGridScale(12);
			panelCariotipagem.getButtonFatorDeGrade().setText("4x");
		} else if(panelCariotipagem.getPanelViewFotograma().getGridScale() == 12) {
			panelCariotipagem.getPanelViewFotograma().setGridScale(6);
			panelCariotipagem.getButtonFatorDeGrade().setText("8x");
		} else {
			panelCariotipagem.getPanelViewFotograma().setGridScale(48);
			panelCariotipagem.getButtonFatorDeGrade().setText("1x");
		}
	}
}
