package com.towel.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

/**
 * Componente que disponibiliza um campo para tratamento de datas com display de
 * um calendario
 * 
 * Criar a referencia do objeto no começo da classe (declaração global)
 * Calendario cl;
 * 
 * Instanciar dentro do construtor e adicionar na tela cl = new
 * Calendario(150,20,w,h,g,this);
 * 
 * No metodo posicionaFoco(int i) (obrigatorio implementa-lo) o case para a data
 * sera sempre 99 case 99: cl.setFocus();
 */
public class CalendarView extends JPanel implements KeyListener,
		ActionListener, FocusListener {
	boolean DataValida = true;
	boolean alterado = false;

	Cal cal;

	JButton bto = new JButton();

	JTextField txt = new JTextField();

	int xCal, yCal;
	JPanel glass;

	int cont = 0;

	/*
	 * variaveis usadas pelas tela que existem data da validade do cartão
	 * 
	 * @Param boolean cadmesano e o paramentro para chamar o construtor do
	 * metodo para a data da validade do cartao String dtmesano varivel que e
	 * atribuida a data String variavel mes atribuido o mes int m varival para
	 * fazer a validação do mes se for > 13 não aceita String ano variavel que é
	 * atribuido o ano da data do cartão
	 */
	boolean cadmesano = false;
	boolean caddiames = false;
	String dtmesano = "";
	String dtdiames = "";
	String mes = "";
	int m = 0;
	int a = 0;
	int as = 0;
	int d = 0;
	String ano = "";
	String dia = "";
	String data;

	// ////////////////////////////////////////////
	/**
	 * Constutor
	 * 
	 * @param x
	 *            - Coluna
	 * @param y
	 *            - Linha
	 * @param xCal
	 *            - largura Frame
	 * @paal - altura Applet Frame
	 * @param JPanel
	 *            - referencia do glassPane
	 */
	public CalendarView(int x, int y, int xCal, int yCal, JPanel glass) {

		this.xCal = xCal;
		this.yCal = yCal;
		this.glass = glass;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setBounds(x, y, 105, 21);
		bto.addActionListener(this);
		txt.addKeyListener(this);
		txt.addFocusListener(this);

	}

	public CalendarView(int x, int y, int xCal, int yCal, JPanel glass,
			boolean mesano, String d) {
		cadmesano = mesano;
		data = d;

		this.xCal = xCal;
		this.yCal = yCal;
		this.glass = glass;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setBounds(x, y, 105, 21);
		txt.addKeyListener(this);
		txt.addFocusListener(this);
	}

	public CalendarView(int x, int y, int xCal, int yCal, JPanel glass,
			boolean diames) {
		caddiames = diames;

		this.xCal = xCal;
		this.yCal = yCal;
		this.glass = glass;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setBounds(x, y, 105, 21);
		txt.addKeyListener(this);
		txt.addFocusListener(this);
	}

	private void jbInit() throws Exception {
		if (cadmesano && !caddiames) {

			txt.setPreferredSize(new Dimension(53, 21));
			txt.setSelectionEnd(7);
			txt.setMinimumSize(new Dimension(53, 21));
			txt.setMaximumSize(new Dimension(53, 21));
			txt.setText("__/____");
			txt.setColumns(7);
			txt.setBounds(new Rectangle(0, 0, 53, 21));
			bto.setFont(new Font("SansSerif", 0, 12));
			bto.setBounds(new Rectangle(73, 0, 32, 21));
			bto.setText("..");
			bto.setFont(new Font("SansSerif", Font.BOLD, 12));
			this.setLayout(null);
			this.add(txt, null);
			this.add(bto, null);
		} else if (caddiames && !cadmesano) {
			txt.setPreferredSize(new Dimension(53, 21));
			txt.setSelectionEnd(5);
			txt.setMinimumSize(new Dimension(53, 21));
			txt.setMaximumSize(new Dimension(53, 21));
			txt.setText("__/__");
			txt.setColumns(5);
			txt.setBounds(new Rectangle(0, 0, 53, 21));
			bto.setFont(new Font("SansSerif", 0, 12));
			bto.setBounds(new Rectangle(73, 0, 32, 21));
			bto.setText("..");
			bto.setFont(new Font("SansSerif", Font.BOLD, 12));
			this.setLayout(null);
			this.add(txt, null);
			this.add(bto, null);
		} else if (!cadmesano && !caddiames) {
			txt.setPreferredSize(new Dimension(73, 21));
			txt.setSelectionEnd(10);
			txt.setMinimumSize(new Dimension(73, 21));
			txt.setMaximumSize(new Dimension(73, 21));
			txt.setText("__/__/____");
			txt.setColumns(10);
			txt.setBounds(new Rectangle(0, 0, 73, 21));
			bto.setFont(new Font("SansSerif", 0, 12));
			bto.setBounds(new Rectangle(73, 0, 32, 21));
			bto.setText("..");
			bto.setFont(new Font("SansSerif", Font.BOLD, 12));
			this.setLayout(null);
			this.add(txt, null);
			this.add(bto, null);
		}
	}

	/**
	 * Metodo para setar o campo texto
	 * 
	 * @param String
	 *            - texto
	 */
	public void setText(String text) {
		txt.setText(text);
	}

	/**
	 * Metodo para retornar o campo texto
	 * 
	 * @return String - texto
	 */
	public String getText() {
		return txt.getText();
	}

	public void actionPerformed(ActionEvent e) {
		String strDia = txt.getText();
		DataValida = Data.isDate(strDia);
		if (DataValida) {
			int dia = Integer.parseInt(strDia.substring(0, 2));
			int mes = Integer.parseInt(strDia.substring(3, 5));
			int ano = Integer.parseInt(strDia.substring(6, 10));

			cal = new Cal(this, xCal, yCal, dia, mes, ano);
		} else {
			cal = new Cal(this, xCal, yCal, 0, 0, 0);
		}
		glass.add(cal, null);
		glass.setVisible(true);
	} // ActionPerformed

	/**
	 * Remove o calendario do glassPane e coloca a data no campo texto
	 * 
	 * @param String
	 *            - data
	 */
	public void removeCalendario(String s) {
		txt.setText(s);
		alterado = true;
		glass.remove(cal);
		cal = null;
		glass.repaint();
		glass.setVisible(false);
	}

	/*
	 * Informa se o campo sofreu alguma alteracao
	 * 
	 * @return boolean
	 */
	public boolean getAlterado() {
		return alterado;
	}

	/*
	 * Informa que os dados foram salvos
	 */
	public void setATualizado() {
		alterado = false;
	}

	// ==================KEYLISTENER=================================================
	/**
	 * Metodo que informa que o campo foi alterado
	 */
	public void keyPressed(KeyEvent k) {
		alterado = true;
	}

	/**
	 * Metodo que valida o que é digitado permitindo somente numeros
	 */
	public void keyTyped(KeyEvent k) {
		if (cadmesano && !caddiames) {
			char c = k.getKeyChar();
			if ((getText().length() > 6) & (!getText().equals("__/____")))
				k.consume();
			else {
				if ((c < '0') | (c > '9'))// & (c != '/'))
					k.consume();
				else {
					if (cont == 0) {
						setText("");
						cont = 1;
					}
					switch (getText().length()) {
					case 2:
						setText(getText() + "/");
						break;
					}
				}
			}
		} else if (caddiames && !cadmesano) {
			char c = k.getKeyChar();
			if ((getText().length() > 4) & (!getText().equals("__/__")))
				k.consume();
			else {
				if ((c < '0') | (c > '9'))// & (c != '/'))
					k.consume();
				else {
					if (cont == 0) {
						setText("");
						cont = 1;
					}
					switch (getText().length()) {
					case 2:
						setText(getText() + "/");
						break;
					}
				}
			}
		} else if (!cadmesano && !caddiames) {
			char c = k.getKeyChar();
			if ((getText().length() > 9) & (!getText().equals("__/__/____")))
				k.consume();
			else {
				if ((c < '0') | (c > '9'))// & (c != '/'))
					k.consume();
				else {
					if (cont == 0) {
						setText("");
						cont = 1;
					}
					switch (getText().length()) {
					case 2:
						setText(getText() + "/");
						break;
					case 5:
						setText(getText() + "/");
						break;
					}
				}
			}
		}
	}

	public void keyReleased(KeyEvent k) {
	}

	// ==================FOCUSKEYLISTENER============================================
	public void focusGained(FocusEvent fe) {
	}

	/**
	 * Quando o componente perde o foco é validado a data
	 */
	public void focusLost(FocusEvent fe) {
		if (cadmesano && !caddiames) {
			if (txt.getText().length() < 7 || txt.getText().length() > 7) {
				JOptionPane.showMessageDialog(null,
						"Digite o mês e ano da data de validade");
			} else {

				dtmesano = txt.getText();
				mes = dtmesano.substring(0, 2);
				ano = dtmesano.substring(3, 7);
				m = Integer.parseInt(mes);
				if (m > 12)
					JOptionPane.showMessageDialog(null, "Mes Inválido");
				if (!ano.equals("") && m <= 12) {
					String anoservidor = data.substring(6, 10);
					a = Integer.parseInt(ano);
					as = Integer.parseInt(anoservidor);
					if (a < as)
						JOptionPane.showMessageDialog(null, "Ano Inválido");
					else {
						ano = dtmesano.substring(5, 7);
						mes = dtmesano.substring(0, 3);
						txt.setText(mes + ano);
					}

				}
			}
		}
		if (caddiames && !cadmesano) {
			if (txt.getText().length() < 5 || txt.getText().length() > 5) {
				JOptionPane.showMessageDialog(null,
						" Digite o dia e mês da data de aniversario");
			} else {
				dtdiames = txt.getText();
				dia = dtdiames.substring(0, 2);
				mes = dtdiames.substring(3, 5);
				m = Integer.parseInt(mes);
				d = Integer.parseInt(dia);
				if (mes.equals("") || m > 12)
					JOptionPane.showMessageDialog(null, "Mes Inválido");
				if (dia.equals("") || d > 31)
					JOptionPane.showMessageDialog(null, "Dia Inválido");
			}
		} else if (!cadmesano && !caddiames) {
			if (!getText().equals("__/__/____") & !getText().equals("")) {
				if (!Data.isDate(getText())) {
					JOptionPane.showMessageDialog(null, "Data Inválida");
				}
			}
		}
	}

	public void setEnabled(boolean t) {
		txt.setEnabled(t);
		bto.setEnabled(t);
	}

	/**
	 * Metodo que deixa o botao Visivel ou Invisivel
	 * 
	 * @Param true = visivel
	 * @Param false = invivel
	 **/
	public void setBotaoVisivel(boolean t) {
		bto.setVisible(t);
	}
}
