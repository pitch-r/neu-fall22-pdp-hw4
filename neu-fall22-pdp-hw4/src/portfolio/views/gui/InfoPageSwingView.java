package portfolio.views.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import portfolio.controllers.InputHandler;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.views.View;

/**
 * This is a view that show the GUI determine page, which implement the View function.
 */
public class InfoPageSwingView implements View {

  private final JFrame frame;

  private final InputHandler inputHandler;
  private final PortfolioWithValue portfolioWithPrice;
  private final String errorMessage;
  private final Double costOfBasis;

  /**
   * This is a constructor that construct a GUI determine page view.
   *
   * @param frame              the frame for GUI
   * @param inputHandler       the controller for handling input
   * @param portfolioWithPrice the object of PortfolioWithValue
   * @param costOfBasis        the cost and basis for this portfolio
   * @param errorMessage       the error message we want to show to the user
   */
  public InfoPageSwingView(JFrame frame, InputHandler inputHandler,
      PortfolioWithValue portfolioWithPrice, Double costOfBasis,
      String errorMessage) {
    this.frame = frame;
    this.inputHandler = inputHandler;
    this.portfolioWithPrice = portfolioWithPrice;
    this.errorMessage = errorMessage;
    this.costOfBasis = costOfBasis;
  }

  private JScrollPane showPortfolioInfor() {
    Vector vData = new Vector();
    Vector vName = new Vector();
    vName.add("Stock");
    vName.add("No. of shares");
    vName.add("Current value");
    for (var entry : portfolioWithPrice.getValues()) {
      Vector row = new Vector();
      row.add(String.valueOf(entry.getSymbol()));
      row.add(String.valueOf(entry.getAmount()));
      row.add("$" + entry.getValue());
      vData.add(row);
    }

    DefaultTableModel model = new DefaultTableModel(vData, vName) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    JTable portfolioTable = new JTable(model);
    portfolioTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
    JScrollPane jsp = new JScrollPane(portfolioTable);
    return jsp;
  }

  @Override
  public void render() {
    frame.setTitle("Portfolio composition and other value at date");
    frame.setSize(600, 600);
    JPanel panelBack = new JPanel();
    panelBack.setLayout(null);
    JButton backButton = new JButton("back");
    backButton.addActionListener(e -> inputHandler.handleInput("back"));
    backButton.setBounds(0, 0, 70, 30);
    panelBack.add(backButton);

    JLabel error = new JLabel(errorMessage);
    error.setForeground(Color.red);

    JPanel panelDate = new JPanel();
    panelDate.setLayout(new FlowLayout());
    panelDate.setSize(500, 20);
    JLabel portfolioName = new JLabel("The date (format:2022-10-10):");
    panelDate.add(portfolioName);

    JTextField dateTextArea = new JTextField(10);
    if (portfolioWithPrice != null) {
      dateTextArea.setText(String.valueOf(portfolioWithPrice.getDate()));
    }
    panelDate.add(dateTextArea);

    JButton checkButton = new JButton("check");
    checkButton.addActionListener(e -> inputHandler.handleInput(dateTextArea.getText()));
    panelDate.add(checkButton);

    JPanel panelShow = new JPanel();
    JPanel panelTotal = new JPanel();
    JPanel panelCost = new JPanel();
    if (portfolioWithPrice != null) {
      JScrollPane jsp = showPortfolioInfor();
      JLabel showLabel = new JLabel("portfolio:");
      panelShow.add(showLabel);
      panelShow.add(jsp);

      JLabel totalLabel = new JLabel("Total value : $" + portfolioWithPrice.getTotalValue());
      panelTotal.add(totalLabel);

      JLabel costLabel = new JLabel("Cost of basis : " + "$" + costOfBasis);
      panelCost.add(costLabel);
    }

    Box vBox = Box.createVerticalBox();
    vBox.setPreferredSize(new Dimension(500, 500));
    vBox.add(panelBack);
    vBox.add(error);
    vBox.add(panelDate);
    vBox.add(panelShow);
    vBox.add(panelTotal);
    vBox.add(panelCost);

    frame.setContentPane(vBox);
    frame.repaint();
    frame.revalidate();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
