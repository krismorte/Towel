package com.towel.swing.table.headerpopup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * @author Vinicius Godoy
 */
public class TableHeaderPopup {
	protected EventListenerList listenerList = new EventListenerList();

	private JTableHeader header = null;
	protected List<HeaderPopup> popups = null;
	private TableModel model = null;
	private int selected = -1;
	private final Map<Integer, Boolean> modified;

	public TableHeaderPopup(final JTableHeader header, final TableModel model) {
		this.header = header;
		this.model = model;

		this.modified = new HashMap<Integer, Boolean>();

		createPopups();
		modifyHeader();
	}

	private void createPopups() {
		popups = new ArrayList<HeaderPopup>();
		for (int i = 0; i < model.getColumnCount(); i++) {
			HeaderPopup headerPopup = new HeaderPopup(header, i);
			headerPopup.addPopupMenuListener(new PopupMenuListener() {

				@Override
				public void popupMenuCanceled(final PopupMenuEvent e) {
					selected = -1;
					header.invalidate();
					header.repaint();
				}

				@Override
				public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
					selected = -1;
					header.invalidate();
					header.repaint();
				}

				@Override
				public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
					header.invalidate();
					header.repaint();
				}
			});

			popups.add(headerPopup);
		}
	}

	public void modifyHeader() {
		if (header.getDefaultRenderer() instanceof FilteredHeaderRenderer) {
			header.setDefaultRenderer(new FilteredHeaderRenderer(
					((FilteredHeaderRenderer) header.getDefaultRenderer())
							.getRenderer()));
		} else {
			header.setDefaultRenderer(new FilteredHeaderRenderer(header
					.getDefaultRenderer()));
		}

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int columnIndex = header.columnAtPoint(e.getPoint());

					if (columnIndex == -1) {
						return;
					}

					int modelIndex = header.getColumnModel()
							.getColumn(columnIndex).getModelIndex();

					if (popups.get(modelIndex).isEmpty()) {
						return;
					}

					Rectangle rect = header.getHeaderRect(columnIndex);
					rect.x = rect.x + rect.width - 17;
					rect.width = 16;

					rect.y = rect.y + rect.height - 17;
					rect.height = 16;

					if (!rect.contains(e.getPoint())) {
						return;
					}

					fireHeaderButtonClicked(modelIndex);

					popups.get(modelIndex).show(columnIndex);
					selected = columnIndex;
				}
			}
		});
	}

	private class FilteredHeaderRenderer implements TableCellRenderer {
		private final TableCellRenderer renderer;

		public FilteredHeaderRenderer(final TableCellRenderer renderer) {
			this.renderer = renderer;
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected,
				final boolean hasFocus, final int row, final int column) {
			Component c = renderer.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			if (!(c instanceof JLabel)) {
				return c;
			}

			int modelIndex = header.getColumnModel().getColumn(column)
					.getModelIndex();
			if (popups.get(modelIndex).isEmpty()) {
				return c;
			}

			JLabel label = (JLabel) c;

			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.weightx = 1.0;

			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			panel.add(c, gridBagConstraints);

			JToggleButton button = new JToggleButton();

			if (modified.get(modelIndex) == null || !modified.get(modelIndex)) {
				button.setIcon(new ImageIcon(getClass().getResource(
						"/images/down.gif")));
			} else {
				button.setIcon(new ImageIcon(getClass().getResource(
						"/images/down_red.gif")));
			}

			button.setPreferredSize(new Dimension(16, 16));
			button.setMaximumSize(new Dimension(16, 16));
			button.setMinimumSize(new Dimension(16, 16));
			button.setFocusable(false);
			button.setSelected(column == selected);

			panel.add(button, new GridBagConstraints());

			Border border = UIManager.getBorder("TableHeader.cellBorder");
			border.getBorderInsets(null).set(0, 2, 1, 1);
			panel.setBorder(label.getBorder());
			label.setBorder(null);

			return panel;
		}

		public TableCellRenderer getRenderer() {
			return renderer;
		}
	}

	public HeaderPopup getPopup(final int modelIndex) {
		return popups.get(modelIndex);
	}

	public void setModified(final int modelIndex, final boolean value) {
		modified.put(modelIndex, value);
	}

	public void addButtonListener(final HeaderButtonListener l) {
		listenerList.add(HeaderButtonListener.class, l);
	}

	protected void fireHeaderButtonClicked(final int modelIndex) {
		Object[] listeners = listenerList.getListenerList();
		HeaderPopupEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == HeaderButtonListener.class) {
				if (e == null) {
					e = new HeaderPopupEvent(this, modelIndex);
				}
				((HeaderButtonListener) listeners[i + 1]).buttonClicked(e);
			}
		}
	}
}
