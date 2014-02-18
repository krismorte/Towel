package com.towel.swing.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import com.towel.cfg.TowelConfig;
import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.io.Closable;
import com.towel.swing.ModalWindow;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;

public class SelectTable<T> {
	private static final String CLOSE_TXT_ATTR = "close_txt_attr";
	private static final String SELECT_TXT_ATTR = "select_txt_attr";

	private List<ObjectSelectListener> listeners;
	private JTable table;
	private Paginator<T> data;
	private ObjectTableModel<T> model;
	private JFrame frame;
	private JPanel content;
	private JButton selectButton, closeButton;
	private JLabel pageLabel;
	private List<Closable> closableHook;
	private TableFilter filter;

	public static final int SINGLE = 0;
	public static final int LIST = 1;

	private int selectType;

	public SelectTable(final ObjectTableModel<T> model2, final List<T> list2) {
		this(model2, new ListPaginator<T>(list2), SINGLE);
	}

	public SelectTable(final ObjectTableModel<T> model2,
			final Paginator<T> list2) {
		this(model2, list2, SINGLE);
	}

	public SelectTable(final ObjectTableModel<T> model,
			final Paginator<T> list, final int selectType) {
		listeners = new ArrayList<ObjectSelectListener>();
		closableHook = new ArrayList<Closable>();

		model.setEditableDefault(false);

		data = list;
		this.model = model;
		model.setData(data.nextResult());

		buildBody();

		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				try {
					updateSelectedObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				dispose();
			}
		});

		table.addMouseListener(new SelectionListener());

		setSelectionType(selectType);

		setLocale(TowelConfig.getInstance().getDefaultLocale());
	}

	public void useTableFilter() {
		filter = new TableFilter(table);
		filter.setLocale(TowelConfig.getInstance().getDefaultLocale());
	}

	private void buildBody() {
		table = new JTable(model);
		frame = new JFrame("Select");

		content = new JPanel();
		pane = new JScrollPane();
		pane.setViewportView(table);

		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.add(pane);
		content.add(getResultScrollPane());
		content.add(createCommandButtons());
	}

	private JPanel createCommandButtons() {
		JPanel buttons = new JPanel();
		buttons.setAlignmentX(.5f);
		selectButton = new JButton("Select");
		closeButton = new JButton("Close");
		buttons.add(selectButton);
		buttons.add(closeButton);
		return buttons;
	}

	public void setSize(final int width, final int height) {
		pane.setPreferredSize(new Dimension(width, height));
		pane.setMinimumSize(new Dimension(width, height));
	}

	public JTable getTable() {
		return table;
	}

	public void closeOnDispose(final Closable close) {
		closableHook.add(close);
	}

	private boolean closed = false;
	private JScrollPane pane;

	public void close() {
		if (closed) {
			return;
		}
		closed = true;
		for (Closable closable : closableHook) {
			closable.close();
		}
	}

	public void setSelectButtonText(final String text) {
		selectButton.setText(text);
	}

	public void setCloseButtonText(final String text) {
		closeButton.setText(text);
	}

	public void setLocale(final Locale locale) {
		ResourceBundle labels = ResourceBundle.getBundle("strings", locale);

		setOptions(labels);
	}

	private void setOptions(final ResourceBundle bundle) {
		setSelectButtonText(bundle.getString(SELECT_TXT_ATTR));
		setCloseButtonText(bundle.getString(CLOSE_TXT_ATTR));
	}

	public void setButtonsText(final String select, final String close) {
		setSelectButtonText(select);
		setCloseButtonText(close);
	}

	public void addObjectSelectListener(final ObjectSelectListener listener) {
		listeners.add(listener);
	}

	public Container getContent() {
		return content;
	}

	public void showModal(final Component parent) {
		try {
			final JDialog dialog = ModalWindow.createDialog(parent,
					getContent(), "Select");
			closeOnDispose(new Closable() {
				@Override
				public void close() {
					dialog.dispose();
				}
			});
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void showSelectTable() {
		showSelectTable("Select");
	}

	public void showSelectTable(final String title) {
		frame = new JFrame(title);
		frame.setContentPane(content);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(2);
		frame.setVisible(true);
		closeOnDispose(new Closable() {
			@Override
			public void close() {
				frame.dispose();
			}

		});
	}

	private void notifyListeners(final SelectEvent evt) {
		for (ObjectSelectListener listener : listeners) {
			listener.notifyObjectSelected(evt.clone());
		}
	}

	public void setSelectionType(final int selectType) {
		this.selectType = selectType;
		table.setSelectionMode(this.selectType == SINGLE ? ListSelectionModel.SINGLE_SELECTION
				: ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public int getSelectType() {
		return selectType;
	}

	public void dispose() {
		close();
		if (frame != null) {
			frame.dispose();
		}
	}

	public JPanel getResultScrollPane() {
		JPanel container = new JPanel();
		pageLabel = new JLabel((new StringBuilder("1/")).append(
				data.getMaxPage() + 1).toString());
		JButton first = new JButton("<<");
		JButton previous = new JButton("<");
		JButton next = new JButton(">");
		JButton last = new JButton(">>");
		container.add(first);
		container.add(previous);
		container.add(pageLabel);
		container.add(next);
		container.add(last);
		first.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				firstResult();
			}
		});
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				previousResult();
			}
		});
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				nextResult();
			}
		});
		last.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				lastResult();
			}
		});
		return container;
	}

	private void firstResult() {
		data.setCurrentPage(0);
		model.setData(data.nextResult());
		pageLabel.setText((new StringBuilder("1/")).append(
				data.getMaxPage() + 1).toString());
	}

	private void previousResult() {
		if (data.getCurrentPage() - 2 < 0) {
			return;
		} else {
			data.setCurrentPage(data.getCurrentPage() - 2);
			model.setData(data.nextResult());
			pageLabel.setText((new StringBuilder(String.valueOf(String
					.valueOf(data.getCurrentPage())))).append("/")
					.append(data.getMaxPage() + 1).toString());
			return;
		}
	}

	private void nextResult() {
		try {
			if (data.getCurrentPage() >= data.getMaxPage()) {
				data.setCurrentPage(data.getMaxPage());
			}
			model.setData(data.nextResult());
			pageLabel.setText((new StringBuilder(String.valueOf(String
					.valueOf(data.getCurrentPage())))).append("/")
					.append(data.getMaxPage() + 1).toString());
		} catch (Exception e) {
			return;
		}
	}

	private void lastResult() {
		data.setCurrentPage(data.getMaxPage());
		model.setData(data.nextResult());
		pageLabel.setText((new StringBuilder(String.valueOf(String.valueOf(data
				.getCurrentPage())))).append("/").append(data.getMaxPage() + 1)
				.toString());
	}

	public void updateSelectedObject() {
		int[] objIndex = table.getSelectedRows();
		int[] realIdx = filter == null ? objIndex : filter
				.getModelRows(objIndex);

		if (objIndex.length == 1) {
			notifyListeners(new SelectEvent(this, model.getValue(realIdx[0])));
		} else {
			List<T> selected = new ArrayList<T>();
			for (int i : realIdx) {
				selected.add(model.getValue(i));
			}
			notifyListeners(new SelectEvent(this, selected));
		}
		dispose();
	}

	public void notifyDataChanged() {
		model.fireTableDataChanged();
	}

	private class SelectionListener extends MouseAdapter {
		@Override
		public void mouseClicked(final MouseEvent e) {
			if (e.getClickCount() == 2) {
				updateSelectedObject();
				dispose();
			}
		}
	}

	public TableModel getModel() {
		return model;
	}

	public void setFont(final Font font) {
		table.setFont(font);
	}

	public void fitColumnsToHeader() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Resizer.fitColumnsByHeader(0, table);
	}
}