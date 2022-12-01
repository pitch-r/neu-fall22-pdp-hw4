package portfolio.views.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
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
import portfolio.models.entities.Transaction;
import portfolio.views.View;

/**
 * This is a view that show the GUI create page, which implement the View function.
 */
public class ScheduleCreatePageSwingView implements View {

  private final JFrame frame;

  private final InputHandler inputHandler;
  private final Map<String, Double> stockList;
  private final String errorMessage;
  private final Boolean isEnd;
  private final List<String> inputBuffer;

  /**
   * This is a constructor that construct a create page view. The error messages will contain "Error
   * Format!", "The share is not a number.", "The shares cannot be negative and 0.", "Symbol not
   * found.", "error!", "The name cannot be end, back, no and yes.", "External API is not ready.
   * Please try again in the next few minutes.", "No stock entered. Please input stock.".
   *
   * @param frame        the frame for GUI
   * @param inputHandler the controller for handling input
   * @param isEnd        if user finish input the portfolio, it will be true. Otherwise, false.
   * @param transactions the map that store the symbol and shares for portfolio.
   * @param errorMessage the error message we want to show to the user.
   */
  public ScheduleCreatePageSwingView(JFrame frame, InputHandler inputHandler,
      Map<String, Double> stockList, boolean isEnd, List<String> inputBuffer,
      List<Transaction> transactions, String errorMessage) {
    this.frame = frame;
    this.inputHandler = inputHandler;
    this.isEnd = isEnd;
    this.inputBuffer = inputBuffer;
    this.stockList = stockList;
    this.errorMessage = errorMessage;
  }

  /**
   * The helper function to show the transaction table in a ScrollPane.
   *
   * @return ScrollPanel that contain the table of transaction
   */
  private JScrollPane showTransaction() {
    Vector vData = new Vector();
    Vector vName = new Vector();
    vName.add("Stock symbol");
    vName.add("Weight");
    for (var entry : stockList.entrySet()) {
      Vector row = new Vector();
      row.add(String.valueOf(entry.getKey()));
      row.add(String.valueOf(entry.getValue()));
      vData.add(row);
    }

    DefaultTableModel model = new DefaultTableModel(vData, vName) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return true;
      }

      @Override
      public void setValueAt(Object value, int row, int col)
      {
          String firstKey = (String) stockList.keySet().toArray()[row];
          inputHandler.handleInput(firstKey + "," + value);
      }
    };
    JTable portfolioTable = new JTable(model);
    portfolioTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
    JScrollPane jsp = new JScrollPane(portfolioTable);
    return jsp;
  }

  @Override
  public void render() {
    frame.setSize(600, 700);
    JPanel panelBack = new JPanel();
    panelBack.setLayout(null);
    JButton backButton = new JButton("back");
    backButton.addActionListener(e -> inputHandler.handleInput("back"));
    backButton.setBounds(0, 0, 70, 30);
    panelBack.add(backButton);

    JLabel error = new JLabel(errorMessage);
    error.setForeground(Color.red);

    JPanel panelCommissionFee = new JPanel();
    panelCommissionFee.setLayout(new FlowLayout());
    JPanel panelSymbol = new JPanel();
    panelSymbol.setLayout(new FlowLayout());
    JPanel panelShares = new JPanel();
    panelShares.setLayout(new FlowLayout());
    JPanel panelShow = new JPanel();
    panelShow.setLayout(new FlowLayout());
    JPanel panelButton = new JPanel();
    panelButton.setLayout(new FlowLayout());

    if (isEnd == false) {
      JLabel symbolLabel = new JLabel("Stock Symbol (ex. AAPL)");
      panelSymbol.add(symbolLabel);
      JTextField symbolTextArea = new JTextField(10);
      panelSymbol.add(symbolTextArea);

      JLabel sharesLabel = new JLabel("Weight: ");
      panelShares.add(sharesLabel);
      JTextField sharesTextArea = new JTextField(10);
      panelShares.add(sharesTextArea);

      if (errorMessage != null && inputBuffer.size() > 0) {
        symbolTextArea.setText(inputBuffer.get(0));
        sharesTextArea.setText(inputBuffer.get(1));
      }

      if (stockList != null) {
        JScrollPane jsp = showTransaction();
        panelShow.add(jsp);
      }

      JButton buttonBuy = new JButton("Add");
      buttonBuy.addActionListener(
          e -> inputHandler.handleInput(symbolTextArea.getText() + "," + sharesTextArea.getText()));
      panelButton.add(buttonBuy);

      JButton buttonFinish = new JButton("Finish");
      buttonFinish.addActionListener(e -> inputHandler.handleInput("yes"));
      panelButton.add(buttonFinish);

    }

    JPanel panelNamed = new JPanel();
    panelNamed.setLayout(new FlowLayout());
    panelNamed.setSize(500, 20);

    if (isEnd) {
      if (stockList != null) {
        JScrollPane jsp = showTransaction();
        panelShow.add(jsp);
      }
      JLabel namedLabel = new JLabel("Name of portfolio:");
      panelNamed.add(namedLabel);
      JTextField namedTextArea = new JTextField(10);
      panelNamed.add(namedTextArea);
      JLabel amountLabel = new JLabel("Amount (USD):");
      panelNamed.add(amountLabel);
      JTextField amountTextArea = new JTextField(10);
      panelNamed.add(amountTextArea);
      JLabel startDateLabel = new JLabel("StartDate (format: 2022-10-10):");
      panelNamed.add(startDateLabel);
      JTextField startDate = new JTextField(10);
      panelNamed.add(startDate);
      JLabel frequency = new JLabel("Frequency (number of days):");
      panelNamed.add(frequency);
      JTextField frequencyTextArea = new JTextField(10);
      panelNamed.add(frequencyTextArea);
      JLabel endDateLabel = new JLabel("EndDate (format: 2022-10-10):");
      panelNamed.add(endDateLabel);
      JTextField endDate = new JTextField(10);
      panelNamed.add(endDate);
      JLabel commissionFee = new JLabel("Commission fee:");
      panelNamed.add(commissionFee);
      JTextField commissionFeeTextArea = new JTextField(10);
      panelNamed.add(commissionFeeTextArea);
      JButton buttonNamed = new JButton("Create & Save to File");
      buttonNamed.addActionListener(e -> inputHandler.handleInput(
          namedTextArea.getText() + "," + amountTextArea.getText() + ","
              + frequencyTextArea.getText() + "," + startDate.getText()
              + "," + endDate.getText() + "," + commissionFeeTextArea.getText()));
      panelNamed.add(buttonNamed);
    }

    Box vBox = Box.createVerticalBox();
    vBox.setPreferredSize(new Dimension(500, 500));
    vBox.add(panelBack);
    vBox.add(error);
    vBox.add(panelSymbol);
    vBox.add(panelShares);
    vBox.add(panelShow);
    vBox.add(panelButton);
    vBox.add(panelNamed);

    frame.setContentPane(vBox);
    frame.repaint();
    frame.revalidate();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
